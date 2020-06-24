package com.example.serverlessmapreducejava.shared;

import java.util.stream.Stream;

public interface InputStrategy {
    Stream<?> extract(Object o, Class<?> type);
}
