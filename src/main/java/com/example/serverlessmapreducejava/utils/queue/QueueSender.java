package com.example.serverlessmapreducejava.utils.queue;

import com.google.api.core.ApiFuture;

public interface QueueSender {
    ApiFuture<String> send(Object object, String topic);
}
