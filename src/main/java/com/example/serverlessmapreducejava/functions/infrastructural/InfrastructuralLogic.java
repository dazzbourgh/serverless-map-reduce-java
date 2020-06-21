package com.example.serverlessmapreducejava.functions.infrastructural;

import com.example.serverlessmapreducejava.domain.Animal;

import java.util.function.Function;

public interface InfrastructuralLogic<BucketEvent, QueueEvent> {
    Function<BucketEvent, String> extractFilePath();

    Function<QueueEvent, Animal> extractAnimal();
}
