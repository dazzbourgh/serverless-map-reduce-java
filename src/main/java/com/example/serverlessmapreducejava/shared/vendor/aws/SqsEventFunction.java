package com.example.serverlessmapreducejava.shared.vendor.aws;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class SqsEventFunction implements Function<LinkedHashMap<String, Object>, Object> {
    private final Function<Object, Object> delegate;

    @Override
    public Object apply(LinkedHashMap<String, Object> stringObjectLinkedHashMap) {
        return delegate.apply(stringObjectLinkedHashMap);
    }
}
