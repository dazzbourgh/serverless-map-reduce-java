package com.example.serverlessmapreducejava.intermediate.function;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.example.serverlessmapreducejava.intermediate.domain.StorageObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Component
@ConditionalOnProperty(value = "provider", havingValue = "aws")
public class AwsInfrastructuralLogic implements InfrastructuralLogic {
    private static final ObjectMapper mapper = new ObjectMapper();
    @Override
    @Bean
    public Function<Map<String, Object>, List<StorageObject>> extractFilePath() {
        return sqsEvent -> getS3Events(sqsEvent)
                .flatMap(s3Event -> s3Event.getRecords().stream())
                .map(S3EventNotification.S3EventNotificationRecord::getS3)
                .map(s3Record -> new StorageObject(s3Record.getBucket().getName(), s3Record.getObject().getKey()))
                .collect(toList());
    }

    @SneakyThrows
    private S3Event getS3Events(String record) {
        return mapper.readValue(record, S3Event.class);
    }

    @SneakyThrows
    private Stream<S3Event> getS3Events(Map<String, Object> record) {
        List<Map<String, Object>> records = (List<Map<String, Object>>) record.get("Records");
        return records.stream()
                .map(r -> r.get("body"))
                .map(body -> (String) body)
                .map(this::getS3Events);
    }
}
