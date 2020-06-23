package com.example.serverlessmapreducejava.shared.gcp.strategy;

import com.example.serverlessmapreducejava.shared.InputStrategy;
import com.example.serverlessmapreducejava.shared.gcp.domain.PubSubEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class PubSubStrategy implements InputStrategy {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Object extract(Object evt, Class<?> type) {
        String data = new String(Base64.getDecoder().decode(((PubSubEvent) evt).getData()));
        return objectMapper.readValue(data, type);
    }
}
