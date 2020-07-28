package com.example.serverlessmapreducejava.intermediate.function;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.example.serverlessmapreducejava.intermediate.domain.StorageObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Component
@ConditionalOnProperty(value = "provider", havingValue = "aws")
public class AwsInfrastructuralLogic implements InfrastructuralLogic {
    private static final ObjectMapper mapper = new ObjectMapper();
    @Override
    @Bean
    public Function<SQSEvent, List<StorageObject>> extractFilePath() {
        return sqsEvent -> sqsEvent.getRecords().stream()
                .flatMap(record -> getS3Event(record).getRecords().stream())
                .map(record -> new StorageObject(record.getS3().getBucket().getName(), record.getS3().getObject().getKey()))
                .collect(toList());
    }

    @SneakyThrows
    private S3Event getS3Event(SQSEvent.SQSMessage record) {
        return mapper.readValue(record.getBody(), S3Event.class);
    }
}
