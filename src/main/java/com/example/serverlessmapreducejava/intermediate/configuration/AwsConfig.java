package com.example.serverlessmapreducejava.intermediate.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
@ConditionalOnProperty(value = "provider", havingValue = "aws")
public class AwsConfig {
    @Bean
    public S3Client amazonS3(@Value("${region}") String region) {
        return S3Client.builder()
                .region(Region.of(region))
                .build();
    }

    @Bean
    public SqsAsyncClient amazonSQSAsyncClient(@Value("${region}") String region) {
        return SqsAsyncClient.builder()
                .region(Region.of(region))
                .build();
    }
}
