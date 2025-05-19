package com.example.riskFinder.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record WaypointMeasurementsResponse(
    Long waypointId,
    String crackId,
    Location location,
    List<Measurement> measurements
) {
    public record Location(double latitude, double longitude) {}
    public record Measurement(LocalDateTime timestamp, double widthMm) {}
}
