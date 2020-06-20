package com.example.serverlessmapreducejava.utils.queue;

import java.util.concurrent.CompletableFuture;

public interface QueueSender {
    CompletableFuture<Void> send(Object object, String topic);
}
