package com.example.serverlessmapreducejava.intermediate.utils.aws;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.example.serverlessmapreducejava.intermediate.utils.QueueSender;
import com.spikhalskiy.futurity.Futurity;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Service
@ConditionalOnProperty(value = "provider", havingValue = "aws")
@AllArgsConstructor
public class SqsQueueSender implements QueueSender {
    private final AmazonSQSAsyncClient sqs;

    @Override
    public CompletableFuture<Void> send(Object object, String topic) {
        var futureResult = sqs.sendMessageAsync(new SendMessageRequest(topic, object.toString()));
        return Futurity.shift(futureResult).thenAccept(emptyConsumer());
    }

    private Consumer<? super SendMessageResult> emptyConsumer() {
        return (Consumer<SendMessageResult>) sendMessageResult -> {
        };
    }
}
