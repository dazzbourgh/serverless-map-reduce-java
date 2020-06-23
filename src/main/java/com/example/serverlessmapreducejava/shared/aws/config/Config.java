package com.example.serverlessmapreducejava.shared.aws.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@Configuration
@ConditionalOnProperty(value = "provider", havingValue = "aws")
public class Config {
    @Bean
    public DynamoDbAsyncClient dynamoDbAsyncClient(@Value("${region}") String region) {
        return DynamoDbAsyncClient.builder()
                .region(Region.of(region))
                .build();
    }
}
