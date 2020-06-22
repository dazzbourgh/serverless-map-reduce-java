package com.example.serverlessmapreducejava.shared;

@FunctionalInterface
public interface PipelineTerminalOperation {
    void accept(Object o);
}
