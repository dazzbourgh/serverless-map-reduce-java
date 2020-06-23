package com.example.serverlessmapreducejava.intermediate.function;

import com.example.serverlessmapreducejava.intermediate.domain.StorageObject;

import java.util.List;
import java.util.function.Function;

public interface InfrastructuralLogic<BucketEvent, QueueEvent> {
    Function<BucketEvent, List<StorageObject>> extractFilePath();
}
