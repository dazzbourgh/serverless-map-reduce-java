package com.example.serverlessmapreducejava.functions.infrastructural;

import com.example.serverlessmapreducejava.domain.aws.AwsEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@ConditionalOnProperty(value = "provider", havingValue = "gcp")
public class AwsInfrastructuralLogic implements InfrastructuralLogic<AwsEvent> {
    @Override
    public Function<AwsEvent, String> extractFilePath() {
        return null;
    }
}
