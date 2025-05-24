package com.example.riskFinder.model;

import java.time.LocalDateTime;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.riskFinder.repository.CrackMeasurementRepository;
import com.example.riskFinder.repository.CrackRepository;
import com.example.riskFinder.repository.WaypointRepository;
import com.example.riskFinder.service.GptImageAnalysisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrackImageEventListener {

    private final WaypointRepository waypointRepository;
    private final CrackRepository crackRepository;
    private final CrackMeasurementRepository crackMeasurementRepository;
    private final GptImageAnalysisService gptImageAnalysisService;

    @Async
    @EventListener
    public void onCrackImageUploaded(CrackImageAnalysisEvent event) {
        log.info("📥 CrackImageAnalysisEvent 수신: imageUrl={}, waypointId={}", event.imageUrl(), event.waypointId());

        try {
            Waypoint waypoint = waypointRepository.findById(event.waypointId())
                .orElseThrow(() -> new IllegalStateException("❌ Waypoint not found: id=" + event.waypointId()));

            Crack crack = crackRepository.findByLatitudeAndLongitudeAndAltitude(
                    waypoint.getLatitude(),
                    waypoint.getLongitude(),
                    waypoint.getAltitude())
                .orElseThrow(() -> new IllegalStateException("❌ Crack not found at waypoint location"));

            GptImageAnalysisResponse gptResult = gptImageAnalysisService.analyze(event.imageUrl());
            log.info("📊 GPT 분석 결과: width={}mm, type={}", gptResult.widthMm(), gptResult.crackType());

            if (crack.getCrackType() == null || crack.getCrackType().isBlank()) {
                crack.setCrackType(gptResult.crackType());
                crackRepository.save(crack);
                log.info("🛠️ CrackType 업데이트 완료: {}", gptResult.crackType());
            }

            CrackMeasurement measurement = CrackMeasurement.builder()
                .crackId(crack.getCrackId())
                .widthMm(gptResult.widthMm())
                .measurementDate(LocalDateTime.now())
                .imageUrl(event.imageUrl())
                .createdAt(LocalDateTime.now())
                .build();

            crackMeasurementRepository.save(measurement);
            log.info("✅ CrackMeasurement 저장 완료: crackId={}, width={}mm", crack.getCrackId(), gptResult.widthMm());

        } catch (Exception e) {
            log.error("🔥 CrackImage 분석 및 저장 실패: {}", e.getMessage(), e);
        }
    }
}
