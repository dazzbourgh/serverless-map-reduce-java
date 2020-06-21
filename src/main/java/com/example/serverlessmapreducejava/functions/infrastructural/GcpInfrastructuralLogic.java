package com.example.serverlessmapreducejava.functions.infrastructural;

import com.example.serverlessmapreducejava.domain.Animal;
import com.example.serverlessmapreducejava.domain.gcp.GcsEvent;
import com.example.serverlessmapreducejava.domain.gcp.PubSubEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@ConditionalOnProperty(value = "provider", havingValue = "gcp")
@AllArgsConstructor
public class GcpInfrastructuralLogic implements InfrastructuralLogic<GcsEvent, PubSubEvent> {
    private final ObjectMapper objectMapper;

    @Override
    @Bean
    public Function<GcsEvent, String> extractFilePath() {
        return event -> String.format("gs://%s/%s",
                event.getJsonPayload().getBucket(),
                event.getJsonPayload().getName());
    }

    @Override
    @Bean
    public Function<PubSubEvent, Animal> extractAnimal() {
        return this::getAnimal;
    }

    @SneakyThrows
    private Animal getAnimal(PubSubEvent event) {
        return objectMapper.readValue(event.getData(), Animal.class);
    }
}
