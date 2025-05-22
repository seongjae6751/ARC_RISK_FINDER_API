package com.example.riskFinder.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
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
        log.info("📍 [SAVE] 요청 도착: lat={}, lon={}, alt={}",
            req.latitude(), req.longitude(), req.altitude());

        Building building = buildingRepository
            .findNearestByLonLat(req.latitude(), req.longitude(), 100)
            .orElseGet(() -> {
                log.info("🔍 [BUILDING] DB에 없음 → 카카오 API 호출 시도");
                return kakaoService.fetchNearestAndConvert(req.latitude(), req.longitude(), 500)
                    .map(b -> {
                        log.info("🏢 [BUILDING] 카카오 응답으로 새 건물 저장: {}", b.getName());
                        return buildingRepository.save(b);
                    })
                    .orElse(null);
            });

        log.info("📦 [WAYPOINT] Building 매핑 상태: {}", building != null ? building.getName() : "null");

        Waypoint wp = Waypoint.builder()
            .latitude(req.latitude())
            .longitude(req.longitude())
            .altitude(req.altitude())
            .building(building)
            .build();

        waypointRepository.save(wp);
        log.info("✅ [WAYPOINT] 저장 완료: id={}", wp.getId());
    }

    public List<WaypointsResponse> getWaypoints() {
        return waypointRepository.findAll().stream()
            .map(wp -> {
                // 가장 최근 Crack 탐지일 조회 (좌표 기준)
                List<Crack> cracks = crackRepository.findByExactLocation(
                    wp.getLatitude(), wp.getLongitude(), wp.getAltitude()
                );

                LocalDate latest = cracks.stream()
                    .map(Crack::getDetectedAt)
                    .filter(d -> d != null)
                    .map(LocalDateTime::toLocalDate)
                    .max(LocalDate::compareTo)
                    .orElse(null);

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
                List<Crack> cracks = crackRepository.findByExactLocation(
                    wp.getLatitude(), wp.getLongitude(), wp.getAltitude()
                );
                List<WaypointImagesResponse.ImageEntry> entries = cracks.stream()
                    .map(c -> measurementRepository.findByCrackId(c.getCrackId())
                        .stream()
                        .max(Comparator.comparing(CrackMeasurement::getMeasurementDate))
                        .map(m -> new WaypointImagesResponse.ImageEntry(m.getImageUrl(), m.getMeasurementDate()))
                        .orElse(null))
                    .filter(e -> e != null)
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
                List<Crack> cracks = crackRepository.findByExactLocation(
                    wp.getLatitude(), wp.getLongitude(), wp.getAltitude()
                );

                List<CrackMeasurement> allMeasurements = cracks.stream()
                    .flatMap(c -> measurementRepository.findByCrackId(c.getCrackId()).stream())
                    .toList();

                List<WaypointMeasurementsResponse.Measurement> measurementList = allMeasurements.stream()
                    .map(m -> new WaypointMeasurementsResponse.Measurement(
                        m.getMeasurementDate(),
                        m.getWidthMm()
                    )).toList();

                return new WaypointMeasurementsResponse(
                    wp.getId(),
                    "WP " + wp.getId(),
                    new WaypointMeasurementsResponse.Location(wp.getLatitude(), wp.getLongitude()),
                    measurementList
                );
            }).toList();
    }
}
