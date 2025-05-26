package com.example.riskFinder.dto;

import com.example.riskFinder.model.WaypointType;

public record WaypointRequest(
    double latitude,
    double longitude,
    double altitude,
    int sequence,
    double heading,
    WaypointType type
) {}
