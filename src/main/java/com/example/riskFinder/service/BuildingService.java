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
        List<Waypoint> waypoints = waypointRepository.findAll()
            .stream()
            .filter(wp -> wp.getBuilding() != null && wp.getBuilding().getId().equals(building.getId()))
            .toList();

        List<BuildingResponse.WaypointResponse> waypointResponses = waypoints.stream()
            .map(wp -> {
                List<Crack> cracks = crackRepository.findByCrackId(wp.getCrackId());
                List<BuildingResponse.WaypointResponse.CrackResponse> crackResponses = cracks.stream()
                    .map(c -> {
                        // CrackMeasurement에서 crackId별 최신 width_mm 조회 (측정일 기준 최신값)
                        CrackMeasurement latestMeasurement = measurementRepository.findByCrackId(c.getCrackId()).stream()
                            .max(Comparator.comparing(CrackMeasurement::getMeasurementDate))
                            .orElse(null);

                        double widthMm = latestMeasurement != null ? latestMeasurement.getWidthMm() : 0.0;

                        return new BuildingResponse.WaypointResponse.CrackResponse(
                            c.getDetectedAt() != null ? c.getDetectedAt().toString() : null,
                            widthMm,
                            c.getImageUrl()
                        );
                    }).toList();

                return new BuildingResponse.WaypointResponse(
                    wp.getId(),
                    wp.getCrackId(),
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
