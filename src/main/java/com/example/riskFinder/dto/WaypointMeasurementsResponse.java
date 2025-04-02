package com.example.riskFinder.dto;

import java.time.LocalDate;
import java.util.List;

public record WaypointMeasurementsResponse(
        Long waypointId,
        String label,
        List<MeasurementEntry> measurements
) {
    public record MeasurementEntry(LocalDate date, double width_mm) {}
}