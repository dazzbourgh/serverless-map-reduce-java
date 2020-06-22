package com.example.serverlessmapreducejava.shared.gcp.config;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryFactory;
import com.google.cloud.bigquery.BigQueryOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "provider", havingValue = "gcp")
public class Config {
    @Bean
    public BigQuery bigQuery() {
        BigQueryFactory bigQueryFactory = new BigQueryOptions.DefaultBigQueryFactory();
        return bigQueryFactory.create(BigQueryOptions.newBuilder().build());
    }
}