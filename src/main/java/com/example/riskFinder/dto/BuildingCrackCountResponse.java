package com.example.riskFinder.dto;

public record BuildingCrackCountResponse(
    Long buildingId,
    String buildingName,
    Long crackCount) {

}
