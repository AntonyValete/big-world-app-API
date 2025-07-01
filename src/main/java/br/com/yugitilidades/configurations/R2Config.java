package br.com.yugitilidades.configurations;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

@Configuration
public class R2Config {
    private final String r2AccessKeyId;
    private final String r2Endpoint;
    private final String r2SecretAccessKey;
    private final String r2Region;

    public R2Config(
        @Value("${cloudflare.r2.access.key.id}") String r2AccessKeyId,
        @Value("${cloudflare.r2.endpoint}") String r2Endpoint,
        @Value("${cloudflare.r2.secret.access.key}") String r2SecretAccessKey,
        @Value("${cloudflare.r2.region}") String r2Region
    ) {
        this.r2AccessKeyId = r2AccessKeyId;
        this.r2Endpoint = r2Endpoint;
        this.r2SecretAccessKey = r2SecretAccessKey;
        this.r2Region = r2Region;
    }

    @Bean
    public S3Client s3Client() {
        AwsCredentials credentials = AwsBasicCredentials.create(r2AccessKeyId, r2SecretAccessKey);

        return S3Client.builder()
                .region(Region.of(r2Region))
                .endpointOverride(URI.create(r2Endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .serviceConfiguration(S3Configuration.builder()
                        .chunkedEncodingEnabled(false)
                        .build())
                .build();
    }
}