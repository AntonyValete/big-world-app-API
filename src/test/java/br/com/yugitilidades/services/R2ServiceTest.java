package br.com.yugitilidades.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class R2ServiceTest {
    private S3Client r2Client;
    private R2Service r2Service;

    @BeforeEach
    void setUp() {
        r2Client = Mockito.mock(S3Client.class);
        r2Service = new R2Service("test-bucket", r2Client);
    }

    @Test
    void uploadFile_success() {
        when(r2Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(null);
        String result = r2Service.uploadFile(new byte[]{1, 2, 3}, "file.jpg");
        assertTrue(result.contains("Uploaded"));
    }

    @Test
    void uploadFile_failure() {
        doThrow(new RuntimeException("fail!")).when(r2Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        String result = r2Service.uploadFile(new byte[]{1, 2, 3}, "file.jpg");
        assertTrue(result.contains("fail!"));
    }

    @Test
    void uploadFileAsync_success() throws Exception {
        when(r2Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(null);
        CompletableFuture<String> future = r2Service.uploadFileAsync(new byte[]{1, 2, 3}, "file.jpg");
        assertTrue(future.get().contains("Uploaded"));
    }
}
