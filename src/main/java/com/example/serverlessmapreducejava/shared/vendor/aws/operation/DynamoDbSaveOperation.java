package com.example.serverlessmapreducejava.shared.vendor.aws.operation;

import com.example.serverlessmapreducejava.shared.PipelineTerminalOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.Map;
import java.util.stream.Collectors;

import static com.example.serverlessmapreducejava.shared.util.FieldsUtil.getFieldsMap;

@Component
@ConditionalOnProperty(value = "provider", havingValue = "aws")
public class DynamoDbSaveOperation implements PipelineTerminalOperation {
    private final String tableName;
    private final DynamoDbAsyncClient dynamoDbAsyncClient;

    public DynamoDbSaveOperation(@Value("${aws.dynamo.table}") String tableName,
                                 DynamoDbAsyncClient dynamoDbAsyncClient) {
        this.tableName = tableName;
        this.dynamoDbAsyncClient = dynamoDbAsyncClient;
    }

    @Override
    @SneakyThrows
    public void accept(Object o) {
        var items = getFieldsMap(o).entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> toValue(entry.getValue())));
        var request = PutItemRequest.builder()
                .item(items)
                .tableName(tableName)
                .build();
        dynamoDbAsyncClient.putItem(request).get();
    }

    private AttributeValue toValue(Object object) {
        if (object instanceof String) {
            return AttributeValue.builder().s((String) object).build();
        } else if (object instanceof Boolean) {
            return AttributeValue.builder().bool((Boolean) object).build();
        } else {
            throw new IllegalArgumentException(object.getClass().getSimpleName());
        }
    }
}
