package com.example.riskFinder.dto;

import java.time.LocalDate;

public record WaypointsResponse(
        Long id,
        String label,
        double lat,
        double lng,
        double altitude,
        LocalDate latestDetectionDate
) {}