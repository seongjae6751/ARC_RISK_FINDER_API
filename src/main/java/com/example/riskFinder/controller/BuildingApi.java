package com.example.riskFinder.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.riskFinder.dto.BuildingResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/buildings")
public interface BuildingApi {
    @Operation(summary = "빌딩 정보 가져오기")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201"),
        @ApiResponse(responseCode = "400"),
        @ApiResponse(responseCode = "404")
    })
    @GetMapping()
    List<BuildingResponse> getBuildings(
        HttpServletRequest request
    );

    @Operation(summary = "특정 빌딩 가져오기")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = ""),
        @ApiResponse(responseCode = "400", description = ""),
        @ApiResponse(responseCode = "404", description = "")
    })
    @GetMapping("/{id}")
    BuildingResponse getBuilding(
        @PathVariable Long id,
        HttpServletRequest request
    );
}
