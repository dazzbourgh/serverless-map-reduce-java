package com.example.serverlessmapreducejava.shared.aws.domain;

import lombok.Data;

import java.util.List;

// TODO
@Data
public class SqsEvent {
    private List<Record> records;
}
