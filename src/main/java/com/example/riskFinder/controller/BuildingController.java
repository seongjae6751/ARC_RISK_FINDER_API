package com.example.riskFinder.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.riskFinder.dto.BuildingResponse;
import com.example.riskFinder.service.BuildingService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/buildings")
public class BuildingController implements BuildingApi{

    private final BuildingService buildingService;

    @GetMapping()
    public List<BuildingResponse> getBuildings(
        HttpServletRequest request
    ) {
        return buildingService.getAllBuildings(request);
    }

    @GetMapping("/{id}")
    public BuildingResponse getBuilding(
        @PathVariable Long id,
        HttpServletRequest request
    ) {
        return buildingService.getBuilding(id, request);
    }
}
