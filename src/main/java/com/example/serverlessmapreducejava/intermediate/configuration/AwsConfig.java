package com.example.serverlessmapreducejava.intermediate.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
@ConditionalOnProperty(value = "provider", havingValue = "aws")
public class AwsConfig {
    @Bean
    public S3Client amazonS3() {
        return S3Client.create();
    }

    @Bean
    public SqsAsyncClient amazonSQSAsyncClient(@Value("${region}") String region) {
        return SqsAsyncClient.create();
    }
}
