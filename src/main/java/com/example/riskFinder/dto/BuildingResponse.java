package com.example.riskFinder.dto;

import java.time.LocalDateTime;
import java.util.List;

public record BuildingResponse(
    Long id,
    String name,
    String address,
    Location location,
    List<WaypointResponse> waypoints
) {
    public record Location(double latitude, double longitude) {}
    public record WaypointResponse(
        Long id,
        String label,
        Location location,
        double altitude,
        List<CrackResponse> cracks
    ) {
        public record CrackResponse(
            String timestamp,
            double widthMm,
            String crackType,
            String imageUrl
        ) {}
    }
}
