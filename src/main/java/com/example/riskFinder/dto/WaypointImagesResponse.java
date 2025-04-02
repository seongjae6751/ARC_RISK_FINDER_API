package com.example.riskFinder.dto;

import java.time.LocalDateTime;
import java.util.List;

public record WaypointImagesResponse(
        Long waypointId,
        String label,
        List<ImageEntry> images
) {
    public record ImageEntry(String imageUrl, LocalDateTime timestamp) {}
}