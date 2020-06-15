package com.example.serverlessmapreducejava.utils.database;

import com.example.serverlessmapreducejava.domain.Classification;

public interface ClassificationDao {
    void save(Classification classification);
}
