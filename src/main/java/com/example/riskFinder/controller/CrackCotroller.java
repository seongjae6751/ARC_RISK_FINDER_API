package com.example.riskFinder.controller;

import com.example.riskFinder.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.riskFinder.service.CrackService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CrackCotroller implements CrackApi{

    private final CrackService crackService;

    @PostMapping("/waypoint")
    public ResponseEntity<Void> saveWaypoint(@RequestBody WaypointRequest request) {
        crackService.save(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/waypoints")
    public ResponseEntity<List<WaypointsResponse>> getWaypoints() {
        List<WaypointsResponse> responses = crackService.getWaypoints();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/waypoints/images")
    public ResponseEntity<List<WaypointImagesResponse>> getWaypointImages() {
        List<WaypointImagesResponse> responses = crackService.getWaypointImages();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/waypoints/measurements")
    public ResponseEntity<List<WaypointMeasurementsResponse>> getWaypointMeasurements() {
        List<WaypointMeasurementsResponse> responses = crackService.getWaypointMeasurements();
        return ResponseEntity.ok(responses);
    }
}
