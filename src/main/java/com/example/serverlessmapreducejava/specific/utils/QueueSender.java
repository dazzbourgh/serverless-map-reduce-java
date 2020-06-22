package com.example.serverlessmapreducejava.specific.utils;

import java.util.concurrent.CompletableFuture;

public interface QueueSender {
    CompletableFuture<Void> send(Object object, String topic);
}
