package com.example.serverlessmapreducejava.shared.gcp.utils;

import com.example.serverlessmapreducejava.shared.PipelineTerminalOperation;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryError;
import com.google.cloud.bigquery.InsertAllRequest;
import com.google.cloud.bigquery.TableId;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.substringAfter;

@Repository
@ConditionalOnProperty(value = "provider", havingValue = "gcp")
public class BigQuerySaveOperation implements PipelineTerminalOperation {
    private final String datasetName;
    private final String tableName;
    private final BigQuery bigQuery;

    public BigQuerySaveOperation(@Value("${gcp.bigquery.dataset.name}") String datasetName,
                                 @Value("${gcp.bigquery.table.name}") String tableName,
                                 BigQuery bigQuery) {
        this.datasetName = datasetName;
        this.tableName = tableName;
        this.bigQuery = bigQuery;
    }

    @Override
    public void accept(Object object) {
        var tableId = TableId.of(datasetName, tableName);
        var rowContent = new HashMap<String, Object>();
        Stream.of(object.getClass().getMethods())
                .filter(method -> method.getName().startsWith("get") || method.getName().startsWith("is"))
                .filter(method -> !method.getName().endsWith("Class"))
                .filter(method -> method.canAccess(object))
                .map(method -> getPair(object, method))
                .forEach(pair -> rowContent.put(pair.left, pair.right));
        var result = bigQuery.insertAll(
                InsertAllRequest.newBuilder(tableId)
                        .addRow(rowContent)
                        .build());
        if (result.hasErrors()) {
            throw new RuntimeException(result.getInsertErrors().values()
                    .stream()
                    .flatMap(Collection::stream)
                    .map(BigQueryError::getMessage)
                    .collect(Collectors.joining("\n"))
            );
        }
    }

    @SneakyThrows
    private ImmutablePair<String, Object> getPair(Object object, Method method) {
        var separator = method.getName().startsWith("get") ? "get" : "is";
        return new ImmutablePair<>(
                substringAfter(method.getName(), separator).toLowerCase(),
                method.invoke(object));
    }
}
