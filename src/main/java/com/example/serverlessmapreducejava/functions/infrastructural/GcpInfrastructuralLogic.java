package com.example.serverlessmapreducejava.functions.infrastructural;

import com.example.serverlessmapreducejava.domain.Animal;
import com.example.serverlessmapreducejava.domain.gcp.GcsEvent;
import com.example.serverlessmapreducejava.domain.gcp.PubSubEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
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

    @Override
    @Bean
    public Function<PubSubEvent, Animal> extractAnimal() {
        return this::getAnimal;
    }

    @SneakyThrows
    private Animal getAnimal(PubSubEvent event) {
        log.info(String.format("Received Pub/Sub event:\n%s", objectMapper.writeValueAsString(event)));
        return objectMapper.readValue(event.getData(), Animal.class);
    }
}
