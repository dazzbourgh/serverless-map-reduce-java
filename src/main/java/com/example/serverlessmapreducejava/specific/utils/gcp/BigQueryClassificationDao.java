package com.example.serverlessmapreducejava.specific.utils.gcp;

import com.example.serverlessmapreducejava.specific.domain.Classification;
import com.example.serverlessmapreducejava.specific.utils.ClassificationDao;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryError;
import com.google.cloud.bigquery.InsertAllRequest;
import com.google.cloud.bigquery.TableId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(value = "provider", havingValue = "gcp")
public class BigQueryClassificationDao implements ClassificationDao {
    private final String datasetName;
    private final String tableName;
    private final BigQuery bigQuery;

    public BigQueryClassificationDao(@Value("${gcp.bigquery.dataset.name}") String datasetName,
                                     @Value("${gcp.bigquery.table.name}") String tableName,
                                     BigQuery bigQuery) {
        this.datasetName = datasetName;
        this.tableName = tableName;
        this.bigQuery = bigQuery;
    }

    @Override
    public void save(Classification classification) {
        var tableId = TableId.of(datasetName, tableName);
        var rowContent = new HashMap<String, Object>();
        rowContent.put("type", classification.getAnimal().getType());
        rowContent.put("wild", classification.getAnimal().isWild());
        rowContent.put("favorite", classification.isFavorite());
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
}