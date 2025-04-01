package com.example.riskFinder.dto;

public record WaypointRequest(
        String crackId,
        double latitude,
        double longitude,
        double altitude
) {}