package com.example.serverlessmapreducejava.shared.aws.strategy;

import com.example.serverlessmapreducejava.shared.InputStrategy;
import com.example.serverlessmapreducejava.shared.aws.domain.SqsEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class SqsStrategy implements InputStrategy {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Stream<?> extract(Object o, Class<?> type) {
        // TODO
        var records = ((SqsEvent) o).getRecords();
        return records.stream();
    }
}
