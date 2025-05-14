package com.example.riskFinder.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.riskFinder.dto.BuildingCrackCountResponse;
import com.example.riskFinder.dto.BuildingCrackMaxWidthResponse;
import com.example.riskFinder.model.Building;
import com.example.riskFinder.repository.BuildingRepository;
import com.example.riskFinder.repository.CrackRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StatService {

    private final BuildingRepository buildingRepository;
    private final CrackRepository crackRepository;

    public long getTotalBuildingCount() {
        return buildingRepository.count();
    }

    public long getTotalCrackCount() {
        return crackRepository.count();
    }

    public List<BuildingCrackCountResponse> getBuildingCrackCounts() {
        return crackRepository.getCrackCountPerBuilding().stream()
            .map(dto -> {
                Building building = buildingRepository.findById(dto.getBuildingId())
                    .orElseThrow(() -> new IllegalArgumentException("Building not found: " + dto.getBuildingId()));
                return new BuildingCrackCountResponse(building.getId(), building.getName(), dto.getCrackCount());
            }).toList();
    }

    public List<BuildingCrackMaxWidthResponse> getBuildingCrackMaxWidths() {
        return crackRepository.getMaxCrackWidthPerBuilding().stream()
            .map(dto -> {
                Building building = buildingRepository.findById(dto.getBuildingId())
                    .orElseThrow(() -> new IllegalArgumentException("Building not found: " + dto.getBuildingId()));
                return new BuildingCrackMaxWidthResponse(building.getId(), building.getName(), dto.getMaxWidth());
            }).toList();
    }
}
