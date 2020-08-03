package com.example.serverlessmapreducejava.shared.vendor.gcp;

import com.example.serverlessmapreducejava.shared.vendor.gcp.domain.PubSubEvent;
import lombok.AllArgsConstructor;

import java.util.function.Function;

@AllArgsConstructor
public class PubSubEventFunction implements Function<PubSubEvent, Object> {
    private final Function<Object, Object> delegate;

    @Override
    public Object apply(PubSubEvent pubSubEvent) {
        return delegate.apply(pubSubEvent);
    }
}
