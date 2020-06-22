package com.example.serverlessmapreducejava.shared;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface PipelineTerminalStage {
    PipelineStageInput input();
    PipelineStageOutput output();

    @Target({METHOD, ANNOTATION_TYPE, PARAMETER})
    @Retention(RUNTIME)
    @interface PipelineStageInput {
        Class<?> type();
        InputOption inputOption();
    }

    @Target({METHOD, ANNOTATION_TYPE, PARAMETER})
    @Retention(RUNTIME)
    @interface PipelineStageOutput {
        OutputOption value();
    }

    enum OutputOption {
        PUB_SUB,
        BIG_QUERY,
        SQS,
        DYNAMO_DB
    }

    enum InputOption {
        PUB_SUB_EVENT,
        GCS_EVENT,
        S3_EVENT,
        SQS_EVENT
    }
}
