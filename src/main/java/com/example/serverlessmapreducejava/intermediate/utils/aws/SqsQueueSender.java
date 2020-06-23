package com.example.serverlessmapreducejava.intermediate.utils.aws;

import com.example.serverlessmapreducejava.intermediate.utils.QueueSender;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Service
@ConditionalOnProperty(value = "provider", havingValue = "aws")
@AllArgsConstructor
public class SqsQueueSender implements QueueSender {
    private final SqsAsyncClient sqs;
    private final ObjectWriter objectWriter;

    @Override
    @SneakyThrows
    public CompletableFuture<Void> send(Object object, String topic) {
        var request = SendMessageRequest.builder()
                .queueUrl(topic)
                .messageBody(objectWriter.writeValueAsString(object))
                .build();
        return sqs.sendMessage(request).thenAccept(emptyConsumer());
    }

    private Consumer<SendMessageResponse> emptyConsumer() {
        return sendMessageResult -> {
        };
    }
}
