package com.example.serverlessmapreducejava;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.utils.FunctionClassUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;

import java.util.function.Function;

@Slf4j
public class SqsEventHandler implements RequestHandler<Object, String> {

    private Function<Object, Message<byte[]>> function;

    public SqsEventHandler() {
        this.start();
    }

    @Override
    @SneakyThrows
    public String handleRequest(Object input, Context context) {
        function.apply(input);
        return "success";
    }

    private void start() {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(FunctionClassUtils.getStartClass());
        Environment environment = applicationContext.getEnvironment();
        String functionName = environment.getProperty("spring.cloud.function.definition");
        FunctionCatalog functionCatalog = applicationContext.getBean(FunctionCatalog.class);

        if (log.isInfoEnabled()) {
            log.info("Locating function: '" + functionName + "'");
        }

        this.function = functionCatalog.lookup(functionName, "application/json");
        Assert.notNull(this.function, "Failed to lookup function " + functionName);

        if (log.isInfoEnabled()) {
            log.info("Located function: '" + functionName + "'");
        }
    }
}
