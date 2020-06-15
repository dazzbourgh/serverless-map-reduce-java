package com.example.serverlessmapreducejava.utils.queue;

public interface QueueSender {
    void send(Object object, String topic);
}
