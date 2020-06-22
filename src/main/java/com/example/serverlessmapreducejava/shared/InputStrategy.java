package com.example.serverlessmapreducejava.shared;

public interface InputStrategy {
    Object extract(Object o, Class<?> type);
}
