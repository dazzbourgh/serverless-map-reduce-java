package com.example.serverlessmapreducejava.specific.utils.storage;

import java.util.stream.Stream;

public interface StorageService {
    Stream<String> get(String path);
}
