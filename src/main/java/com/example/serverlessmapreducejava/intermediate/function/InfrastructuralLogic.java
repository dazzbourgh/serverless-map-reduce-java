package com.example.serverlessmapreducejava.intermediate.function;

import java.util.function.Function;

public interface InfrastructuralLogic<BucketEvent, QueueEvent> {
    Function<BucketEvent, String> extractFilePath();
}
