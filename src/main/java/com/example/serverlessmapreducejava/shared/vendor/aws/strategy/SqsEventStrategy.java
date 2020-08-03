package com.example.serverlessmapreducejava.shared.vendor.aws.strategy;

import com.example.serverlessmapreducejava.shared.InputStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
@Slf4j
public class SqsEventStrategy implements InputStrategy {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Stream<?> extract(Object o, Class<?> type) {
        var map = (Map<String, Object>) o;
        var event = (List<Map<String, Object>>) map.get("Records");
        return event.stream()
                .map(recordMap -> recordMap.get("body").toString())
                .map(body -> map(type, body))
                .peek(animal -> log.info("Mapped animal: {}", animal));
    }

    @SneakyThrows
    private Object map(Class<?> type, String body) {
        return objectMapper.readValue(body, type);
    }
}
