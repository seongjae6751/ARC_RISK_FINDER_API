package com.example.riskFinder.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface UploadApi {

    @Operation(summary = "파일 업로드")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = ""),
        @ApiResponse(responseCode = "400", description = ""),
        @ApiResponse(responseCode = "404", description = "")
    })
    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Map<String, String>> uploadFile(
        @RequestParam String accessKey,
        @RequestParam String domain,
        @RequestPart MultipartFile file
    );
}
