package com.example.serverlessmapreducejava.intermediate.utils;

import java.util.stream.Stream;

public interface StorageService {
    Stream<String> get(String path);
}
