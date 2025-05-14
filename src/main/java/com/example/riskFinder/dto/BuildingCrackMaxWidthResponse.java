package com.example.riskFinder.dto;

public record BuildingCrackMaxWidthResponse(
    Long buildingId,
    String buildingName,
    Double maxCrackWidthMm) {

}
