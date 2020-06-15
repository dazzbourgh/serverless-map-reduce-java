package com.example.serverlessmapreducejava.functions.infrastructural;

import com.example.serverlessmapreducejava.domain.Classification;
import com.example.serverlessmapreducejava.domain.gcp.GcsEvent;
import com.example.serverlessmapreducejava.utils.database.ClassificationDao;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;

@Component
@ConditionalOnProperty(value = "provider", havingValue = "gcp")
public class GcpInfrastructuralLogic implements InfrastructuralLogic<GcsEvent> {

    private final ClassificationDao classificationDao;

    public GcpInfrastructuralLogic(ClassificationDao classificationDao) {
        this.classificationDao = classificationDao;
    }

    @Override
    @Bean
    public Function<GcsEvent, String> extractFilePath() {
        return event -> String.format("gs://%s/%s",
                event.getJsonPayload().getBucket(),
                event.getJsonPayload().getName());
    }

    @Override
    @Bean
    public Consumer<Classification> store() {
        return classificationDao::save;
    }
}
