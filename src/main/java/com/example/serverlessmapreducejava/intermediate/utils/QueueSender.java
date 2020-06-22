package com.example.serverlessmapreducejava.intermediate.utils;

import java.util.concurrent.CompletableFuture;

public interface QueueSender {
    CompletableFuture<Void> send(Object object, String topic);
}
