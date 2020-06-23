package com.example.serverlessmapreducejava.intermediate.function;

import com.example.serverlessmapreducejava.intermediate.domain.StorageObject;
import com.example.serverlessmapreducejava.shared.gcp.domain.GcsEvent;
import com.example.serverlessmapreducejava.shared.gcp.domain.PubSubEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

import static java.util.Collections.singletonList;

@Component
@ConditionalOnProperty(value = "provider", havingValue = "gcp")
@Slf4j
public class GcpInfrastructuralLogic implements InfrastructuralLogic<GcsEvent, PubSubEvent> {
    @Override
    @Bean
    public Function<GcsEvent, List<StorageObject>> extractFilePath() {
        return event -> singletonList(new StorageObject(event.getBucket(), event.getName()));
    }
}
