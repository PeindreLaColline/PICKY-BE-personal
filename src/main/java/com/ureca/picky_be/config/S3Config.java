package com.ureca.picky_be.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@Getter
public class S3Config {
    @Value("${spring.config.activate.on-profile}")
    private String environment;

    @Value("${spring.s3.file-dir}")
    private String rootDir;
    private String fileDir;

    @Value("${spring.s3.access-key}")
    private String accessKey;

    @Value("${spring.s3.secret-key}")
    private String secretKey;

    @Value("${spring.s3.bucket}")
    private String bucket;

    @Value("${spring.s3.region}")
    private String region;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of(region))
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of(region))
                .build();
    }

    public void getLocalFileDir(){
        this.fileDir = System.getProperty("user.dir") + this.rootDir;
    }

    public void getProdFileDir(){
        this.fileDir = System.getProperty("user.dir") + this.rootDir;
    }
}
