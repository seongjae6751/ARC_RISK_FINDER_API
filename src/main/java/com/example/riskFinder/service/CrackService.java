package com.example.riskFinder.service;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.riskFinder.dto.WaypointRequest;
import com.example.riskFinder.model.Waypoint;
import com.example.riskFinder.repository.WaypointRepository;
import org.springframework.stereotype.Service;

import com.example.riskFinder.dto.CrackRequest;
import com.example.riskFinder.model.Crack;
import com.example.riskFinder.repository.CrackRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrackService {

    private final CrackRepository repository;
    private final WaypointRepository waypointRepository;

    public void save(WaypointRequest request) {
        Waypoint waypoint = Waypoint.builder()
                .crackId(request.crackId())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .altitude(request.altitude())
                .build();

        waypointRepository.save(waypoint);
    }
}
