package com.example.serverlessmapreducejava.configuration;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryFactory;
import com.google.cloud.bigquery.BigQueryOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GcpConfig {
    @Bean
    public BigQuery bigQuery() {
        BigQueryFactory bigQueryFactory = new BigQueryOptions.DefaultBigQueryFactory();
        return bigQueryFactory.create(BigQueryOptions.newBuilder().build());
    }
}
