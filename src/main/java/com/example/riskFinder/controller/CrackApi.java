package com.example.riskFinder.controller;

import com.example.riskFinder.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

public interface CrackApi {

    @Operation(summary = "크랙 감지 하면 저장")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = ""),
        @ApiResponse(responseCode = "400", description = ""),
        @ApiResponse(responseCode = "404", description = "")
    })
    @PostMapping("waypoint")
    ResponseEntity<Void> saveWaypoint(
            @RequestBody WaypointRequest request
    );

    @Operation(summary = "웨이포인트 날짜 별 목록 및 좌표 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = ""),
            @ApiResponse(responseCode = "400", description = ""),
            @ApiResponse(responseCode = "404", description = "")
    })
    @GetMapping("/waypoints")
    ResponseEntity<List<WaypointsResponse>> getWaypoints();

    @Operation(summary = "특정 웨이포인트 별 사진 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = ""),
            @ApiResponse(responseCode = "400", description = ""),
            @ApiResponse(responseCode = "404", description = "")
    })
    @GetMapping("/waypoints/images")
    ResponseEntity<List<WaypointImagesResponse>> getWaypointImages();

    @Operation(summary = "빌딩별 waypoint에 대한 균열 너비 시계열 데이터 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = ""),
            @ApiResponse(responseCode = "400", description = ""),
            @ApiResponse(responseCode = "404", description = "")
    })
    @GetMapping("/buildings/{id}/waypoints/measurements")
    ResponseEntity<List<WaypointMeasurementsResponse>> getWaypointMeasurementsByBuilding(
        @PathVariable Long id
    );
}
