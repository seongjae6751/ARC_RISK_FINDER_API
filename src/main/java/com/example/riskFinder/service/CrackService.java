package com.example.riskFinder.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.riskFinder.dto.*;
import com.example.riskFinder.model.CrackMeasurement;
import com.example.riskFinder.model.Waypoint;
import com.example.riskFinder.repository.CrackMeasurementRepository;
import com.example.riskFinder.repository.WaypointRepository;
import org.springframework.stereotype.Service;

import com.example.riskFinder.model.Crack;
import com.example.riskFinder.repository.CrackRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrackService {

    private final CrackRepository crackRepository;
    private final WaypointRepository waypointRepository;
    private final CrackMeasurementRepository measurementRepository;

    public void save(WaypointRequest request) {
        Waypoint waypoint = Waypoint.builder()
                .crackId(request.crackId())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .altitude(request.altitude())
                .build();

        waypointRepository.save(waypoint);
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

    public List<WaypointMeasurementsResponse> getWaypointMeasurements() {
        return waypointRepository.findAll().stream()
                .map(wp -> {
                    List<CrackMeasurement> measures = measurementRepository.findByCrackId(wp.getCrackId());
                    List<WaypointMeasurementsResponse.MeasurementEntry> entries = measures.stream()
                            .map(m -> new WaypointMeasurementsResponse.MeasurementEntry(
                                    m.getMeasurementDate().toLocalDate(), m.getWidthMm()))
                            .toList();

                    return new WaypointMeasurementsResponse(
                            wp.getId(),
                            "WP " + wp.getId(),
                            entries
                    );
                }).toList();
    }
}
