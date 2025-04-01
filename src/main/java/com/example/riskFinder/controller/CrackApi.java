package com.example.riskFinder.controller;

import com.example.riskFinder.dto.WaypointRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.riskFinder.dto.CrackRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface CrackApi {

    @Operation(summary = "크랙 감지 하면 저장", description = "크랙을 탐지하면 저장합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "프로젝트 생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없음")
    })
    @PostMapping("waypoint")
    ResponseEntity<Void> saveWaypoint(
            @RequestBody WaypointRequest request
    );
}
