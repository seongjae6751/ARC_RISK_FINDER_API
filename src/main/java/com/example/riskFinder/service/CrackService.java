package com.example.riskFinder.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.riskFinder.dto.*;
import com.example.riskFinder.model.Building;
import com.example.riskFinder.model.CrackMeasurement;
import com.example.riskFinder.model.Waypoint;
import com.example.riskFinder.repository.BuildingRepository;
import com.example.riskFinder.repository.CrackMeasurementRepository;
import com.example.riskFinder.repository.WaypointRepository;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.riskFinder.model.Crack;
import com.example.riskFinder.repository.CrackRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrackService {

    private final CrackRepository crackRepository;
    private final WaypointRepository waypointRepository;
    private final CrackMeasurementRepository measurementRepository;
    private final BuildingRepository buildingRepository;
    private final KakaoMapService kakaoService;
    private final GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);

    @Transactional
    public void save(WaypointRequest req) {

        log.info("📍 [SAVE] 요청 도착: crackId={}, lat={}, lon={}, alt={}",
            req.crackId(), req.latitude(), req.longitude(), req.altitude());

        Building building = buildingRepository
            .findNearest(req.latitude(), req.longitude(), 100)
            .orElseGet(() -> {
                log.info("🔍 [BUILDING] DB에 없음 → 카카오 API 호출 시도");
                return kakaoService.fetchNearestAndConvert(req.latitude(), req.longitude(), 500)
                    .map(b -> {
                        log.info("🏢 [BUILDING] 카카오 응답으로 새 건물 저장: {}", b.getName());
                        return buildingRepository.save(b);
                    })
                    .orElseGet(() -> {
                        log.warn("⚠️ [BUILDING] 카카오에서도 건물 찾지 못함 (null 반환)");
                        return null;
                    });
            });

        log.info("📦 [WAYPOINT] Building 매핑 상태: {}", building != null ? building.getName() : "null");

        Waypoint wp = Waypoint.builder()
            .crackId(req.crackId())
            .latitude(req.latitude())
            .longitude(req.longitude())
            .altitude(req.altitude())
            .building(building)
            .build();

        waypointRepository.save(wp);
        log.info("✅ [WAYPOINT] 저장 완료: id={}, crackId={}", wp.getId(), wp.getCrackId());
    }

    public List<WaypointsResponse> getWaypoints() {
        return waypointRepository.findAll().stream()
                .map(wp -> {
                    LocalDate latest = crackRepository.findLatestDetectionDate(wp.getCrackId());
                    return new WaypointsResponse(
                            wp.getId(),
                            "WP " + wp.getId(),
                            wp.getLatitude(),
                            wp.getLongitude(),
                            wp.getAltitude(),
                            latest
                    );
                }).toList();
    }

    public List<WaypointImagesResponse> getWaypointImages() {
        return waypointRepository.findAll().stream()
                .map(wp -> {
                    List<Crack> cracks = crackRepository.findByCrackId(wp.getCrackId());
                    List<WaypointImagesResponse.ImageEntry> entries = cracks.stream()
                            .map(c -> new WaypointImagesResponse.ImageEntry(c.getImageUrl(), c.getDetectedAt()))
                            .toList();

                    return new WaypointImagesResponse(
                            wp.getId(),
                            "WP " + wp.getId(),
                            entries
                    );
                }).toList();
    }

    public List<WaypointMeasurementsResponse> getWaypointMeasurementsByBuilding(Long buildingId) {
        Building building = buildingRepository.findById(buildingId)
            .orElseThrow(() -> new IllegalArgumentException("Building not found: " + buildingId));

        return waypointRepository.findByBuildingId(buildingId).stream()
            .map(wp -> {
                List<CrackMeasurement> measurements = measurementRepository.findByCrackId(wp.getCrackId());
                List<WaypointMeasurementsResponse.Measurement> measurementList = measurements.stream()
                    .map(m -> new WaypointMeasurementsResponse.Measurement(
                        m.getMeasurementDate(),
                        m.getWidthMm()
                    ))
                    .toList();

                return new WaypointMeasurementsResponse(
                    wp.getId(),
                    wp.getCrackId(),
                    new WaypointMeasurementsResponse.Location(wp.getLatitude(), wp.getLongitude()),
                    measurementList
                );
            }).toList();
    }
}
