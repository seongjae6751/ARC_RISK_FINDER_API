package com.example.riskFinder.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.riskFinder.dto.UploadFileResponse;
import com.example.riskFinder.model.CrackImageAnalysisEvent;
import com.example.riskFinder.model.S3Client;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final S3Client s3Client;
    private final ApplicationEventPublisher eventPublisher;

    public UploadFileResponse upload(String domain, MultipartFile file, Long waypointId) {
        try {
            String filePath = generateFilePath(domain, Objects.requireNonNull(file.getOriginalFilename()));
            UploadFileResponse response = s3Client.uploadFile(filePath, file.getBytes());

            CrackImageAnalysisEvent event = new CrackImageAnalysisEvent(response.fileUrl(), waypointId);
            eventPublisher.publishEvent(event);

            return response;
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }

    private String generateFilePath(String domain, String fileNameExt) {
        String[] parts = fileNameExt.split("\\.");
        String ext = parts[parts.length - 1];
        String base = String.join("", Arrays.copyOf(parts, parts.length - 1));
        String uuid = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        return String.format("upload/%s/%d/%02d/%02d/%s_%s.%s",
            domain, now.getYear(), now.getMonthValue(), now.getDayOfMonth(), base, uuid, ext);
    }
}
