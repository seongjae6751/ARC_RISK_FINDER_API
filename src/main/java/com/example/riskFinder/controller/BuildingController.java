package com.example.riskFinder.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.riskFinder.dto.BuildingResponse;
import com.example.riskFinder.service.BuildingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/buildings")
public class BuildingController implements BuildingApi{

    private final BuildingService buildingService;

    @GetMapping
    public List<BuildingResponse> getBuildings() {
        return buildingService.getAllBuildings();
    }

    @GetMapping("/{id}")
    public BuildingResponse getBuilding(@PathVariable Long id) {
        return buildingService.getBuilding(id);
    }
}
