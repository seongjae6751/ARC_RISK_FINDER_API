package com.example.riskFinder.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.riskFinder.dto.CrackDetectionRequest;
import com.example.riskFinder.service.CrackDetectionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/crack")
@RequiredArgsConstructor
public class CrackCotroller {

    private final CrackDetectionService crackDetectionService;

    @PostMapping("/detection")
    public ResponseEntity<Void> saveDetection(@RequestBody CrackDetectionRequest request) {
        crackDetectionService.save(request);
        return ResponseEntity.ok().build();
    }
}
