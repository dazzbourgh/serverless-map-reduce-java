package com.example.serverlessmapreducejava.specific.function.infrastructure;

import java.util.function.Function;

public interface InfrastructuralLogic<BucketEvent, QueueEvent> {
    Function<BucketEvent, String> extractFilePath();
}
