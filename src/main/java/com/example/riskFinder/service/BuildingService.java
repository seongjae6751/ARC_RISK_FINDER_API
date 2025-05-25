package com.example.riskFinder.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.riskFinder.dto.BuildingResponse;
import com.example.riskFinder.model.Building;
import com.example.riskFinder.model.Crack;
import com.example.riskFinder.model.CrackMeasurement;
import com.example.riskFinder.model.Waypoint;
import com.example.riskFinder.repository.BuildingRepository;
import com.example.riskFinder.repository.CrackMeasurementRepository;
import com.example.riskFinder.repository.CrackRepository;
import com.example.riskFinder.repository.WaypointRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final WaypointRepository waypointRepository;
    private final CrackRepository crackRepository;
    private final CrackMeasurementRepository measurementRepository;

    public List<BuildingResponse> getAllBuildings(HttpServletRequest request) {
        return buildingRepository.findAll().stream()
            .map(b -> convertToResponse(b, request)) // 파라미터 두 개니까 람다식으로 써야 함
            .toList();
    }

    public BuildingResponse getBuilding(Long id, HttpServletRequest request) {
        Building building = buildingRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Building not found with id: " + id));
        return convertToResponse(building, request);
    }

    private BuildingResponse convertToResponse(Building building, HttpServletRequest request) {
        // 1. 서버 base URL 동적으로 생성 (final로 선언)
        final String serverBaseUrl = buildServerBaseUrl(request);

        // 2. 해당 건물의 모든 웨이포인트 조회
        List<Waypoint> waypoints = waypointRepository.findAll().stream()
            .filter(wp -> wp.getBuilding() != null && wp.getBuilding().getId().equals(building.getId()))
            .toList();

        // 3. 웨이포인트 응답 변환
        List<BuildingResponse.WaypointResponse> waypointResponses = waypoints.stream()
            .map(wp -> {
                List<Crack> cracks = crackRepository.findByExactLocation(
                    wp.getLatitude(), wp.getLongitude(), wp.getAltitude()
                );

                List<BuildingResponse.WaypointResponse.CrackResponse> crackResponses = cracks.stream()
                    .flatMap(c -> measurementRepository.findByCrackId(c.getCrackId()).stream()
                        .map(m -> {
                            String encodedImageUrl = URLEncoder.encode(m.getImageUrl(), StandardCharsets.UTF_8);
                            String proxiedUrl = serverBaseUrl + "/proxy?url=" + encodedImageUrl;

                            return new BuildingResponse.WaypointResponse.CrackResponse(
                                m.getMeasurementDate().toString(),
                                m.getWidthMm(),
                                c.getCrackType(),
                                proxiedUrl
                            );
                        })
                    ).toList();

                return new BuildingResponse.WaypointResponse(
                    wp.getId(),
                    "WP " + wp.getId(),
                    new BuildingResponse.Location(wp.getLatitude(), wp.getLongitude()),
                    crackResponses
                );
            }).toList();

        // 4. 최종 BuildingResponse 반환
        return new BuildingResponse(
            building.getId(),
            building.getName(),
            building.getAddress(),
            new BuildingResponse.Location(
                building.getLocation().getY(), // 위도
                building.getLocation().getX()  // 경도
            ),
            waypointResponses
        );
    }

    // 서버 base URL을 동적으로 구성하는 헬퍼 메서드
    private String buildServerBaseUrl(HttpServletRequest request) {
        String base = request.getScheme() + "://" + request.getServerName();
        int port = request.getServerPort();
        if (port != 80 && port != 443) {
            base += ":" + port;
        }
        return base;
    }
}
