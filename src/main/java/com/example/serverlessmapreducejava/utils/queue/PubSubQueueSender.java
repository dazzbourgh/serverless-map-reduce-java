package com.example.serverlessmapreducejava.utils.queue;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "provider", havingValue = "gcp")
public class PubSubQueueSender implements QueueSender {
    @Override
    public void send(Object object, String topic) {

    }
}
