package com.example.serverlessmapreducejava.functions.infrastructural;

import com.example.serverlessmapreducejava.domain.gcp.GcsEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@ConditionalOnProperty(value = "provider", havingValue = "gcp")
public class GcpInfrastructuralLogic implements InfrastructuralLogic<GcsEvent> {
    @Override
    @Bean
    public Function<GcsEvent, String> extractFilePath() {
        return event -> String.format("gs://%s/%s",
                event.getJsonPayload().getBucket(),
                event.getJsonPayload().getName());
    }
}
