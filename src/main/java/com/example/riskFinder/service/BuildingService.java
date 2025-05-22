package com.example.riskFinder.service;

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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final WaypointRepository waypointRepository;
    private final CrackRepository crackRepository;
    private final CrackMeasurementRepository measurementRepository;

    public List<BuildingResponse> getAllBuildings() {
        return buildingRepository.findAll().stream()
            .map(this::convertToResponse)
            .toList();
    }

    public BuildingResponse getBuilding(Long id) {
        Building building = buildingRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Building not found with id: " + id));
        return convertToResponse(building);
    }

    private BuildingResponse convertToResponse(Building building) {
        List<Waypoint> waypoints = waypointRepository.findAll().stream()
            .filter(wp -> wp.getBuilding() != null && wp.getBuilding().getId().equals(building.getId()))
            .toList();

        List<BuildingResponse.WaypointResponse> waypointResponses = waypoints.stream()
            .map(wp -> {
                List<Crack> cracks = crackRepository.findByExactLocation(
                    wp.getLatitude(), wp.getLongitude(), wp.getAltitude()
                );

                List<BuildingResponse.WaypointResponse.CrackResponse> crackResponses = cracks.stream()
                    .flatMap(c -> measurementRepository.findByCrackId(c.getCrackId()).stream()
                        .map(m -> new BuildingResponse.WaypointResponse.CrackResponse(
                            m.getMeasurementDate().toString(),
                            m.getWidthMm(),
                            m.getImageUrl(),
                            c.getCrackType() // 추가된 필드
                        ))
                    ).toList();

                return new BuildingResponse.WaypointResponse(
                    wp.getId(),
                    "WP " + wp.getId(),
                    new BuildingResponse.Location(wp.getLatitude(), wp.getLongitude()),
                    crackResponses
                );
            }).toList();

        return new BuildingResponse(
            building.getId(),
            building.getName(),
            new BuildingResponse.Location(
                building.getLocation().getY(),
                building.getLocation().getX()
            ),
            waypointResponses
        );
    }
}
