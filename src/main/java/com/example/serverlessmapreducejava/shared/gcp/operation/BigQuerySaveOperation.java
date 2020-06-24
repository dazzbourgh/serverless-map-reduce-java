package com.example.serverlessmapreducejava.shared.gcp.operation;

import com.example.serverlessmapreducejava.shared.PipelineTerminalOperation;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryError;
import com.google.cloud.bigquery.InsertAllRequest;
import com.google.cloud.bigquery.TableId;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.serverlessmapreducejava.shared.util.FieldsUtil.getFieldsMap;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.substringAfter;

@Component
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
        Map<String, Object> fields = getFieldsMap(object);
        var result = bigQuery.insertAll(
                InsertAllRequest.newBuilder(tableId)
                        .addRow(fields)
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
}
