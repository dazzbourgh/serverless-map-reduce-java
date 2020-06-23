package com.example.serverlessmapreducejava.intermediate.function;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.example.serverlessmapreducejava.intermediate.domain.StorageObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Component
@ConditionalOnProperty(value = "provider", havingValue = "aws")
public class AwsInfrastructuralLogic implements InfrastructuralLogic<S3Event, Void> {
    @Override
    @Bean
    public Function<S3Event, List<StorageObject>> extractFilePath() {
        return s3Event -> s3Event.getRecords().stream()
                .map(record -> new StorageObject(record.getS3().getBucket().getName(), record.getS3().getObject().getKey()))
                .collect(toList());
    }
}
