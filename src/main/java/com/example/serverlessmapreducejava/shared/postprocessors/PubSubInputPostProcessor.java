package com.example.serverlessmapreducejava.shared.postprocessors;

import com.example.serverlessmapreducejava.specific.domain.gcp.PubSubEvent;
import com.example.serverlessmapreducejava.shared.annotation.PubSubInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Set;
import java.util.function.Function;

@Component
public class PubSubInputPostProcessor implements BeanPostProcessor {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    private Set<String> beans;

    @PostConstruct
    private void init() {
        beans = Set.of(context.getBeanNamesForAnnotation(PubSubInput.class));
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beans.contains(beanName)) {
            var annotation = context.findAnnotationOnBean(beanName, PubSubInput.class);
            return new Function<PubSubEvent, Object>() {
                @Override
                @SneakyThrows
                public Object apply(PubSubEvent o) {
                    String data = new String(Base64.getDecoder().decode(o.getData()));
                    var animal = objectMapper.readValue(data, annotation.value());
                    return ((Function) bean).apply(animal);
                }
            };
        } else {
            return bean;
        }
    }
}
