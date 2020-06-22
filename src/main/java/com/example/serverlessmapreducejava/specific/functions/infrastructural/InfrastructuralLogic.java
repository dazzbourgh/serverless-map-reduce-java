package com.example.serverlessmapreducejava.specific.functions.infrastructural;

import java.util.function.Function;

public interface InfrastructuralLogic<BucketEvent, QueueEvent> {
    Function<BucketEvent, String> extractFilePath();
}
