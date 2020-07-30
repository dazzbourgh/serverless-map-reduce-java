package com.example.serverlessmapreducejava.intermediate.function;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.example.serverlessmapreducejava.intermediate.domain.S3Event;
import com.example.serverlessmapreducejava.intermediate.domain.StorageObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Collection;
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
        return sqsEvent -> sqsEvent.getRecords()
                .stream()
                .map(SQSEvent.SQSMessage::getBody)
                .map(this::getS3Events)
                .map(S3Event::getRecords)
                .flatMap(Collection::stream)
                .map(S3Event.S3Record::getS3)
                .map(s3 -> new StorageObject(s3.getBucket().getName(), s3.getObject().getKey()))
                .collect(toList());
    }

    @SneakyThrows
    private S3Event getS3Events(String record) {
        return mapper.readValue(record, S3Event.class);
    }
}
