package com.example.serverlessmapreducejava.shared.gcp.postprocessor;

import com.example.serverlessmapreducejava.shared.PipelineTerminalStage;
import com.example.serverlessmapreducejava.shared.gcp.postprocessor.strategy.InputStrategy;
import com.example.serverlessmapreducejava.shared.gcp.domain.PubSubEvent;
import com.example.serverlessmapreducejava.shared.gcp.utils.BigQueryDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static com.example.serverlessmapreducejava.shared.PipelineTerminalStage.InputOption.PUB_SUB_EVENT;
import static com.example.serverlessmapreducejava.shared.PipelineTerminalStage.OutputOption.BIG_QUERY;

@Component
public class PipelineTerminalStagePostProcessor implements BeanPostProcessor {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BigQueryDao bigQueryDao;
    private Set<String> beans;
    @Autowired
    private List<InputStrategy> inputStrategies;

    @PostConstruct
    private void init() {
        beans = Set.of(context.getBeanNamesForAnnotation(PipelineTerminalStage.class));
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beans.contains(beanName)) {
            var pipelineTerminalStage = context.findAnnotationOnBean(beanName, PipelineTerminalStage.class);
            var input = pipelineTerminalStage.input();
            var pipelineStageOutput = pipelineTerminalStage.output();

            var inputOption = input.inputOption();
            var inputType = input.type();
            var outputOption = pipelineStageOutput.value();
            // TODO: need better handling
            if (PUB_SUB_EVENT.equals(inputOption)) {
                return new Function<PubSubEvent, Object>() {
                    @Override
                    @SneakyThrows
                    public Void apply(PubSubEvent o) {
                        inputStrategies.stream()
                                .filter(inputStrategy -> inputStrategy.getClass().getSimpleName().startsWith("Pub"))
                                .findFirst()
                                .map(strategy -> strategy.extract(o, inputType))
                                .map(extractedData -> ((Function) bean).apply(extractedData))
                                .ifPresent(processedData -> {
                                    if (BIG_QUERY.equals(outputOption)) {
                                        bigQueryDao.save(processedData);
                                    }
                                });
                        // TODO: intentionally returns null as the value is never used, but need to see if there is a better approach
                        // Can't return Consumer<PubSub>, since the initial type is Function<Animal, Classification>,
                        // and can't use Consumer<Animal> as initial type since need to return mapped value of Classification
                        return null;
                    }
                };
                // TODO: finish for others
            } else throw new IllegalArgumentException();
        } else {
            return bean;
        }
    }
}
