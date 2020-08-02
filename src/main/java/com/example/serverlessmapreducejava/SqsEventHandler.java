package com.example.serverlessmapreducejava;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.catalog.FunctionInspector;
import org.springframework.cloud.function.utils.FunctionClassUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class SqsEventHandler implements RequestHandler<Object, String> {

    private static Log logger = LogFactory.getLog(SqsEventHandler.class);
    private ConfigurableApplicationContext applicationContext;
    private ObjectMapper mapper;

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

    public static void main(String[] args) throws JsonProcessingException {
        var handler = new SqsEventHandler();
        var request = "{\n" +
                "  \"Records\": [\n" +
                "    {\n" +
                "      \"messageId\": \"19dd0b57-b21e-4ac1-bd88-01bbb068cb78\",\n" +
                "      \"receiptHandle\": \"MessageReceiptHandle\",\n" +
                "      \"body\": \"{\\\"type\\\":\\\"cat\\\",\\\"wild\\\":false}}\",\n" +
                "      \"attributes\": {\n" +
                "        \"ApproximateReceiveCount\": \"1\",\n" +
                "        \"SentTimestamp\": \"1523232000000\",\n" +
                "        \"SenderId\": \"123456789012\",\n" +
                "        \"ApproximateFirstReceiveTimestamp\": \"1523232000001\"\n" +
                "      },\n" +
                "      \"messageAttributes\": {},\n" +
                "      \"md5OfBody\": \"7b270e59b47ff90a553787216d55d91d\",\n" +
                "      \"eventSource\": \"aws:sqs\",\n" +
                "      \"eventSourceARN\": \"arn:{partition}:sqs:{region}:123456789012:MyQueue\",\n" +
                "      \"awsRegion\": \"{region}\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        var event = new ObjectMapper().readValue(request, Map.class);
        handler.handleRequest(event, null);
    }

    private void start() {
        applicationContext = SpringApplication.run(FunctionClassUtils.getStartClass());
        Environment environment = applicationContext.getEnvironment();
        String functionName = environment.getProperty("spring.cloud.function.definition");
        FunctionCatalog functionCatalog = applicationContext.getBean(FunctionCatalog.class);
        this.mapper = applicationContext.getBean(ObjectMapper.class);
        this.configureObjectMapper();

        if (logger.isInfoEnabled()) {
            logger.info("Locating function: '" + functionName + "'");
        }

        this.function = functionCatalog.lookup(functionName, "application/json");
        Assert.notNull(this.function, "Failed to lookup function " + functionName);

        if (!StringUtils.hasText(functionName)) {
            FunctionInspector inspector = applicationContext.getBean(FunctionInspector.class);
            functionName = inspector.getRegistration(this.function).getNames().toString();
        }

        if (logger.isInfoEnabled()) {
            logger.info("Located function: '" + functionName + "'");
        }
    }

    private void configureObjectMapper() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                    throws IOException {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(jsonParser.getValueAsLong());
                return calendar.getTime();
            }
        });
        mapper.registerModule(module);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }
}