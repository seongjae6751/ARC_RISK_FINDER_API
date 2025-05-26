package com.example.riskFinder.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.riskFinder.model.GptImageAnalysisResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptImageAnalysisService {

    private final RestTemplate restTemplate;

    @Value("${gpt.api.url}")
    private String gptApiUrl;

    @Value("${openai.api.key}")
    private String openaiApiKey;

    public GptImageAnalysisResponse analyze(String imageUrl) {
        log.info("📡 GPT 요청 URL = {}", gptApiUrl);

        Map<String, Object> payload = Map.of(
            "model", "gpt-4o",
            "messages", List.of(
                Map.of(
                    "role", "user",
                    "content", List.of(
                        Map.of("type", "image_url", "image_url", Map.of("url", imageUrl)),
                        Map.of("type", "text", "text",
                            "이 이미지는 건물 외벽을 약 1.5미터 거리에서 촬영한 것입니다. " +
                                "이 균열의 양 끝 픽셀 좌표를 추출해서 그 거리(픽셀 간 거리)를 계산하고, mm로 환산해 주세요. " +
                                "똑같은 사진을 넣으면 항상 똑같은 답이 나오게 일정해야 해요. " +
                                "균열의 형태가 가로형, 세로형, 경사형, 망상형 중 무엇에 해당하는지도 함께 판단해 주세요. " +
                                "다음과 같은 JSON 형식으로만 응답해주세요: { \"width_mm\": 숫자, \"crack_type\": 문자열 }"
                        )
                    )
                )
            ),
            "max_tokens", 300
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);

        try {
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                gptApiUrl, requestEntity, String.class
            );

            log.info("📥 GPT 응답 = {}", response.getBody());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode contentNode = root.get("choices").get(0).get("message").get("content");

            // ✅ 1. 문자열로 꺼냄
            String contentText = contentNode.asText().trim();

            // ✅ 2. 마크다운 백틱 제거
            if (contentText.startsWith("```")) {
                contentText = contentText
                    .replaceAll("(?i)```json", "")  // ```json 또는 ```JSON 제거
                    .replaceAll("```", "")          // 나머지 백틱 제거
                    .trim();
            }

            // ✅ 3. 이제 안전하게 파싱 가능
            JsonNode result = mapper.readTree(contentText);

            return new GptImageAnalysisResponse(
                result.get("width_mm").asDouble(),
                result.get("crack_type").asText()
            );

        } catch (Exception e) {
            log.error("❌ GPT Vision 호출 실패: {}", e.getMessage(), e);
            throw new RuntimeException("GPT 이미지 분석에 실패했습니다.", e);
        }
    }
}
