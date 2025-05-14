package com.example.riskFinder.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;

import com.example.riskFinder.dto.BuildingCrackCountResponse;
import com.example.riskFinder.dto.BuildingCrackMaxWidthResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface StatApi {

    @Operation(summary = "총 건물 수")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = ""),
        @ApiResponse(responseCode = "400", description = ""),
        @ApiResponse(responseCode = "404", description = "")
    })
    @GetMapping("/buildings/count")
    Map<String, Long> getTotalBuildingCount();

    @Operation(summary = "총 크랙 수")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = ""),
        @ApiResponse(responseCode = "400", description = ""),
        @ApiResponse(responseCode = "404", description = "")
    })
    @GetMapping("/cracks/count")
    Map<String, Long> getTotalCrackCount();

    @Operation(summary = "건물별 균열 수 순위")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = ""),
        @ApiResponse(responseCode = "400", description = ""),
        @ApiResponse(responseCode = "404", description = "")
    })
    @GetMapping("/buildings/cracks")
    List<BuildingCrackCountResponse> getBuildingCrackCounts();

    @Operation(summary = "건물 별 균열 최대 폭 순위")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = ""),
        @ApiResponse(responseCode = "400", description = ""),
        @ApiResponse(responseCode = "404", description = "")
    })
    @GetMapping("/buildings/cracks/max-width")
    List<BuildingCrackMaxWidthResponse> getBuildingCrackMaxWidths();
}
