package com.example.serverlessmapreducejava.utils.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@ConditionalOnProperty(value = "provider", havingValue = "gcp")
public class PubSubQueueSender implements QueueSender {
    private final String projectId;
    private final ObjectWriter ow;

    public PubSubQueueSender(@Value("${gcp.project.id}") String projectId, ObjectWriter ow) {
        this.projectId = projectId;
        this.ow = ow;
    }

    @Override
    @SneakyThrows
    public void send(Object object, String topic) {
        TopicName topicName = TopicName.of(projectId, topic);

        Publisher publisher = null;
        try {
            String message = ow.writeValueAsString(object);
            publisher = Publisher.newBuilder(topicName).build();
            ByteString data = ByteString.copyFromUtf8(message);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
            publisher.publish(pubsubMessage);
        } finally {
            if (publisher != null) {
                publisher.shutdown();
                publisher.awaitTermination(1, TimeUnit.MINUTES);
            }
        }
    }
}
