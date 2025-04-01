package com.example.riskFinder.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.riskFinder.dto.CrackDetectionRequest;
import com.example.riskFinder.model.CrackDetection;
import com.example.riskFinder.repository.CrackDetectionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrackDetectionService {

    private final CrackDetectionRepository repository;

    public void save(CrackDetectionRequest request) {
        CrackDetection entity = CrackDetection.builder()
            .crackId(request.crackId() != null ? request.crackId() : UUID.randomUUID().toString())
            .latitude(request.latitude())
            .longitude(request.longitude())
            .altitude(request.altitude())
            .imageUrl(request.imageUrl())
            .detectedAt(LocalDateTime.now())
            .build();
        repository.save(entity);
    }
}
