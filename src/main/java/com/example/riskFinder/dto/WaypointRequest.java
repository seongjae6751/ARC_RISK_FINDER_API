package com.example.riskFinder.dto;

public record WaypointRequest(
        double latitude,
        double longitude,
        double altitude
) {}
