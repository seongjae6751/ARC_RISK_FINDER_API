package com.example.riskFinder.service;

import java.util.Optional;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.riskFinder.model.Building;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoMapService {

    @Value("${kakao.api.key}") private String apiKey;
    private final RestTemplate rest = new RestTemplate();
    private final GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);

    public Optional<Building> fetchNearestAndConvert(double lat, double lon, int radius) {

        String categoryCode = "OL7";  // 필수!
        String url = String.format(
            "https://dapi.kakao.com/v2/local/search/category.json?" +
                "category_group_code=%s&x=%f&y=%f&radius=%d&sort=distance",
            categoryCode, lon, lat, radius
        );

        log.info("📡 [KakaoMapService] 요청 URL = {}", url);

        HttpHeaders h = new HttpHeaders();
        h.set("Authorization", "KakaoAK " + apiKey);

        JsonNode response = rest.exchange(url, HttpMethod.GET,
            new HttpEntity<>(h), JsonNode.class).getBody();

        log.info("📨 [KakaoMapService] 응답 JSON = {}", response);

        JsonNode first = response.path("documents").path(0);

        if (first.isMissingNode() || first.isEmpty()) {
            log.warn("⚠️ [KakaoMapService] 검색 결과 없음 (documents 배열이 비어있음)");
            return Optional.empty();
        }

        String name = first.path("place_name").asText();
        String addr = first.path("road_address_name").asText(first.path("address_name").asText());
        double y = first.path("y").asDouble();
        double x = first.path("x").asDouble();

        log.info("🏢 [KakaoMapService] 추출된 건물 정보: name={}, addr={}, x={}, y={}",
            name, addr, x, y);

        Coordinate coord = new Coordinate(x, y);
        Point point = gf.createPoint(coord);
        point.setSRID(4326);

        return Optional.of(
            Building.builder()
                .name(name.isBlank() ? addr : name)
                .address(addr)
                .location(point)
                .build()
        );
    }
}
