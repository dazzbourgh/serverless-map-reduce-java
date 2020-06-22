package com.example.serverlessmapreducejava.specific.function.infrastructure;

import com.example.serverlessmapreducejava.specific.domain.gcp.GcsEvent;
import com.example.serverlessmapreducejava.specific.domain.gcp.PubSubEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@ConditionalOnProperty(value = "provider", havingValue = "gcp")
@AllArgsConstructor
@Slf4j
public class GcpInfrastructuralLogic implements InfrastructuralLogic<GcsEvent, PubSubEvent> {
    private final ObjectMapper objectMapper;

    @Override
    @Bean
    public Function<GcsEvent, String> extractFilePath() {
        return event -> String.format("gs://%s/%s",
                event.getBucket(),
                event.getName());
    }
}
