package com.example.serverlessmapreducejava.shared;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface PipelineStage {
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
        BIG_QUERY("bigQuerySaveOperation"),
        DYNAMO_DB("dynamoDbSaveOperation");

        private final String name;

        public String getName() {
            return name;
        }

        OutputOption(String name) {
            this.name = name;
        }
    }

    enum InputOption {
        PUB_SUB_EVENT("pubSubStrategy"),
        SQS_EVENT("sqsEventStrategy");

        private final String name;

        public String getName() {
            return name;
        }

        InputOption(String name) {
            this.name = name;
        }
    }
}
