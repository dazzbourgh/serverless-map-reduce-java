package com.example.serverlessmapreducejava.shared.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface PubSubInput {
    /**
     * @return class describing the object that needs to be extracted from Pub/Sub message body.
     */
    Class<?> value();
}
