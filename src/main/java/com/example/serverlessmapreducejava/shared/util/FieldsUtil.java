package com.example.serverlessmapreducejava.shared.util;

import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.substringAfter;

public class FieldsUtil {
    public static Map<String, Object> getFieldsMap(Object object) {
        return Stream.of(object.getClass().getMethods())
                .filter(method -> method.getName().startsWith("get") || method.getName().startsWith("is"))
                .filter(method -> !method.getName().endsWith("Class"))
                .filter(method -> method.canAccess(object))
                .map(method -> getPair(object, method))
                .collect(toMap(Pair::getKey, Pair::getValue));
    }

    @SneakyThrows
    private static ImmutablePair<String, Object> getPair(Object object, Method method) {
        var separator = method.getName().startsWith("get") ? "get" : "is";
        return new ImmutablePair<>(
                substringAfter(method.getName(), separator).toLowerCase(),
                method.invoke(object));
    }
}
