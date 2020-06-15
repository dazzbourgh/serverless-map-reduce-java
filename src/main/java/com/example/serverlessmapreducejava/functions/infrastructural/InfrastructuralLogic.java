package com.example.serverlessmapreducejava.functions.infrastructural;

import com.example.serverlessmapreducejava.domain.Classification;

import java.util.function.Consumer;
import java.util.function.Function;

public interface InfrastructuralLogic<Event> {
    Function<Event, String> extractFilePath();

    Consumer<Classification> store();
}
