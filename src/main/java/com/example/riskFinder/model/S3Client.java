package com.example.riskFinder.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.riskFinder.dto.UploadFileResponse;

@Component
public class S3Client {

    private final AmazonS3 amazonS3;
    private final String bucketName;
    private final String domainUrlPrefix;

    public S3Client(
        AmazonS3 amazonS3,
        @Value("${cloud.aws.s3.bucket}") String bucketName,
        @Value("${s3.custom_domain}") String domainUrlPrefix
    ) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
        this.domainUrlPrefix = domainUrlPrefix;
    }

    public UploadFileResponse uploadFile(String uploadFilePath, byte[] fileData) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileData.length);
        metadata.setContentType(detectMimeType(uploadFilePath));

        PutObjectRequest request = new PutObjectRequest(
            bucketName,
            uploadFilePath,
            new ByteArrayInputStream(fileData),
            metadata
        );

        amazonS3.putObject(request);

        return new UploadFileResponse(domainUrlPrefix + uploadFilePath);
    }

    private String detectMimeType(String fileName) {
        if (fileName.endsWith(".png")) return "image/png";
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) return "image/jpeg";
        if (fileName.endsWith(".gif")) return "image/gif";
        if (fileName.endsWith(".pdf")) return "application/pdf";
        return "application/octet-stream";
    }

    public void deleteFile(String s3Key) {
        amazonS3.deleteObject(bucketName, s3Key);
    }

    public String extractKeyFromUrl(String url) {
        if (!url.startsWith(domainUrlPrefix)) {
            throw new IllegalArgumentException("잘못된 S3 URL입니다: " + url);
        }
        return url.substring(domainUrlPrefix.length());
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getDomainUrlPrefix() {
        return domainUrlPrefix;
    }
}
