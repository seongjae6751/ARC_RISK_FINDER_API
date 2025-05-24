package com.example.riskFinder.model;

public record CrackImageAnalysisEvent(
    String imageUrl,
    Long waypointId
) {

}
