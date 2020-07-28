package com.example.serverlessmapreducejava.intermediate.function;

import com.example.serverlessmapreducejava.intermediate.domain.StorageObject;

import java.util.List;
import java.util.function.Function;

public interface InfrastructuralLogic {
    <T> Function<T, List<StorageObject>> extractFilePath();
}
