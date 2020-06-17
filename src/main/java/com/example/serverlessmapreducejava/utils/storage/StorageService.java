package com.example.serverlessmapreducejava.utils.storage;

import java.util.stream.Stream;

public interface StorageService {
    Stream<String> get(String path);
}
