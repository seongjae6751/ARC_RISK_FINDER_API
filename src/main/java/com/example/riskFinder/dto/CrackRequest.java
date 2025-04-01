package com.example.riskFinder.dto;

public record CrackRequest(
    String crackId,
    double latitude,
    double longitude,
    double altitude,
    String imageUrl
) {}
