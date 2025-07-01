package br.com.yugitilidades.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@Log4j2
public class R2Service {
    private final String bucketName;

    private final S3Client r2Client;

    public R2Service(@Value("${cloudflare.r2.bucket.name}") String bucketName, S3Client r2Client) {
        this.bucketName = bucketName;
        this.r2Client = r2Client;
    }

    public String uploadFile(byte[] fileBytes, String fileName) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType("image/jpeg")
                    .build();

            r2Client.putObject(putObjectRequest, RequestBody.fromBytes(fileBytes));
            log.info("File uploaded successfully to R2: key={}", fileName);
            return fileName + " Uploaded.";
        } catch (Exception e) {
            log.error("Error uploading file to R2", e);
            return e.getMessage();
        }
    }

    @Async
    public CompletableFuture<String> uploadFileAsync(byte[] fileBytes, String fileName) {
        return CompletableFuture.supplyAsync(() -> uploadFile(fileBytes, fileName));
    }
}
