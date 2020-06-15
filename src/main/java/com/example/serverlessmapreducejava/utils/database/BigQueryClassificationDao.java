package com.example.serverlessmapreducejava.utils.database;

import com.example.serverlessmapreducejava.domain.Classification;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(value = "provider", havingValue = "gcp")
public class BigQueryClassificationDao implements ClassificationDao {
    @Override
    public void save(Classification classification) {

    }
}
