package com.example.riskFinder.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.riskFinder.dto.BuildingCrackCountResponse;
import com.example.riskFinder.dto.BuildingCrackMaxWidthResponse;
import com.example.riskFinder.service.StatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stats")
public class StatController implements StatApi{

    private final StatService statService;

    @GetMapping("/buildings/count")
    public Map<String, Long> getTotalBuildingCount() {
        return Map.of("totalBuildings", statService.getTotalBuildingCount());
    }

    @GetMapping("/cracks/count")
    public Map<String, Long> getTotalCrackCount() {
        return Map.of("totalCracks", statService.getTotalCrackCount());
    }

    @GetMapping("/buildings/cracks")
    public List<BuildingCrackCountResponse> getBuildingCrackCounts() {
        return statService.getBuildingCrackCounts();
    }

    @GetMapping("/buildings/cracks/max-width")
    public List<BuildingCrackMaxWidthResponse> getBuildingCrackMaxWidths() {
        return statService.getBuildingCrackMaxWidths();
    }
}
