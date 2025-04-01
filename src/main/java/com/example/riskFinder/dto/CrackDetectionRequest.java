package com.example.riskFinder.dto;

public record CrackDetectionRequest(
    String crackId,
    double latitude,
    double longitude,
    double altitude,
    String imageUrl
) {}
