package com.example.riskFinder.controller;

import com.example.riskFinder.dto.WaypointRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.riskFinder.dto.CrackRequest;
import com.example.riskFinder.service.CrackService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CrackCotroller implements CrackApi{

    private final CrackService crackService;

    @PostMapping("waypoint")
    public ResponseEntity<Void> saveWaypoint(@RequestBody WaypointRequest request) {
        crackService.save(request);
        return ResponseEntity.ok().build();
    }
}
