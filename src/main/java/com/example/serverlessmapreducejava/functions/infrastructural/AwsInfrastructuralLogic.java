package com.example.serverlessmapreducejava.functions.infrastructural;

import com.example.serverlessmapreducejava.domain.Animal;
import com.example.serverlessmapreducejava.domain.aws.AwsEvent;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@ConditionalOnProperty(value = "provider", havingValue = "gcp")
public class AwsInfrastructuralLogic implements InfrastructuralLogic<AwsEvent, Void> {
    @Override
    public Function<AwsEvent, String> extractFilePath() {
        return null;
    }

    @Override
    public Function<Void, Animal> extractAnimal() {
        return null;
    }
}
