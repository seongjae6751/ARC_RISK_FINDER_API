package com.example.riskFinder.dto;

import java.time.LocalDate;

import com.example.riskFinder.model.WaypointType;

public record WaypointsResponse(
    Long id,
    String label,
    double lat,
    double lng,
    double altitude,
    LocalDate latestDetectionDate,
    int sequence,
    double heading,
    WaypointType type
) {}
