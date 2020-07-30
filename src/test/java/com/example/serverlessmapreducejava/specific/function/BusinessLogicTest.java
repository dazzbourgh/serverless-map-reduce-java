package com.example.serverlessmapreducejava.specific.function;

import com.example.serverlessmapreducejava.intermediate.domain.StorageObject;
import com.example.serverlessmapreducejava.intermediate.utils.QueueSender;
import com.example.serverlessmapreducejava.intermediate.utils.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class BusinessLogicTest {
    public static final String BUCKET = "bucket";
    public static final String KEY = "key";
    private BusinessLogic businessLogic;
    private QueueSender queueSender;
    private StorageService storageService;

    @BeforeEach
    public void init() {
        queueSender = mock(QueueSender.class);
        storageService = mock(StorageService.class);
        businessLogic = new BusinessLogic(queueSender, "topic", storageService);
    }

    @Test
    void read() {
        var sut = businessLogic.read();
        when(storageService.get(BUCKET, KEY))
                .thenReturn(Stream.of("type,wild", "cat,false"));
        when(queueSender.send(any(), eq("topic"))).thenReturn(CompletableFuture.completedFuture(null));

        sut.accept(singletonList(new StorageObject(BUCKET, KEY)));

        verify(queueSender).send(any(), eq("topic"));
    }

    @Test
    void classify() {
    }
}