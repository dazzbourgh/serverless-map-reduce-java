package com.example.serverlessmapreducejava.shared.gcp.postprocessor.strategy;

public interface InputStrategy {
    Object extract(Object o, Class<?> type);
}
