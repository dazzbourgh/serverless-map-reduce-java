package com.example.serverlessmapreducejava.specific.log;

import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class QueueSenderLogger {
    @Autowired
    public ObjectWriter objectWriter;

    @Pointcut("execution(* com.example.serverlessmapreducejava.specific.utils.QueueSender.send(..))")
    public void beforeQueueSenderSend() {}

    @Before("beforeQueueSenderSend() && args(object, topic)")
    @SneakyThrows
    public void logging(Object object, String topic) {
        log.info(String.format("Sending message to Pub/Sub, topic: %s, message:\n%s",
                topic, objectWriter.writeValueAsString(object)));
    }
}
