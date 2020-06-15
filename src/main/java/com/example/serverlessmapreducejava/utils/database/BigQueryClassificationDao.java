package com.example.serverlessmapreducejava.utils.database;

import com.example.serverlessmapreducejava.domain.Classification;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.InsertAllRequest;
import com.google.cloud.bigquery.TableId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@ConditionalOnProperty(value = "provider", havingValue = "gcp")
public class BigQueryClassificationDao implements ClassificationDao {
    private final String datasetName;
    private final String tableName;
    private final BigQuery bigQuery;

    public BigQueryClassificationDao(@Value("${gcp.bigquery.dataset.name") String datasetName,
                                     @Value("${gcp.bigquery.table.name")String tableName,
                                     BigQuery bigQuery) {
        this.datasetName = datasetName;
        this.tableName = tableName;
        this.bigQuery = bigQuery;
    }

    @Override
    public void save(Classification classification) {
        TableId tableId = TableId.of(datasetName, tableName);
        Map<String, Object> rowContent = new HashMap<>();
        rowContent.put("type", classification.getAnimal().getType());
        rowContent.put("wild", classification.getAnimal().isWild());
        rowContent.put("favorite", classification.isFavorite());
        bigQuery.insertAll(
                InsertAllRequest.newBuilder(tableId)
                        .addRow(rowContent)
                        .build());
    }
}
