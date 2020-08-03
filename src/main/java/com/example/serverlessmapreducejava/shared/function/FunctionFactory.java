package com.example.serverlessmapreducejava.shared.function;

import com.example.serverlessmapreducejava.shared.PipelineStage;
import com.example.serverlessmapreducejava.shared.vendor.aws.SqsEventFunction;
import com.example.serverlessmapreducejava.shared.vendor.gcp.PubSubEventFunction;
import org.springframework.stereotype.Component;

import java.util.function.Function;

// This class is a workaround required to trick BeanFactoryAwareFunctionRegistry::discoverFunctionType,
// which resolves type from the method signature in case Object is used as generic parameter
@Component
public class FunctionFactory {
    public Function<?, ?> create(Function<Object, Object> delegate, PipelineStage.InputOption inputOption) {
        switch (inputOption) {
            case PUB_SUB_EVENT:
                return new PubSubEventFunction(delegate);
            case SQS_EVENT:
                return new SqsEventFunction(delegate);
            default:
                throw new RuntimeException("Unknown input option: " + inputOption);
        }
    }
}
