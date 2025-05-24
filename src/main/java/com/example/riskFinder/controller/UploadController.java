package com.example.riskFinder.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.riskFinder.dto.UploadFileResponse;
import com.example.riskFinder.service.UploadService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/upload")
@RestController
public class UploadController implements UploadApi {

    private final UploadService uploadService;

    @Value("${app.upload.access-key}")
    private String fixedAccessKey;

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadFile(
        @RequestParam String accessKey,
        @RequestParam String domain,
        @RequestParam("waypointId") Long waypointId,
        @RequestPart MultipartFile file
    ) {
        if (!fixedAccessKey.equals(accessKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "인증 실패: 유효하지 않은 accessKey입니다."));
        }

        UploadFileResponse response = uploadService.upload(domain, file, waypointId); // ⚠ 여기 변경
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(Map.of("file_url", response.fileUrl())
            );
    }
}
