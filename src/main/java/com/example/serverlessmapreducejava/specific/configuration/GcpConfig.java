package com.example.serverlessmapreducejava.specific.configuration;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryFactory;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "provider", havingValue = "gcp")
public class GcpConfig {
    @Bean
    public BigQuery bigQuery() {
        BigQueryFactory bigQueryFactory = new BigQueryOptions.DefaultBigQueryFactory();
        return bigQueryFactory.create(BigQueryOptions.newBuilder().build());
    }

    @Bean
    public Storage storage() {
        return StorageOptions.newBuilder().build().getService();
    }
}
