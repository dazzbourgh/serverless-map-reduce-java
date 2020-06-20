package com.example.serverlessmapreducejava.functions.infrastructural;

import java.util.function.Function;

public interface InfrastructuralLogic<Event> {
    Function<Event, String> extractFilePath();
}
