package com.epam.devrelish;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Random;

public class InjectRandomIntAnnotationConfigurator implements ObjectConfigurator {
    private Random random = new Random();

    @Override
    @SneakyThrows
    public void configure(Object t) {
        for (Field field : t.getClass().getDeclaredFields()) {
            InjectRandomInt annotation = field.getAnnotation(InjectRandomInt.class);
            if (annotation != null) {
                int min = annotation.min();
                int max = annotation.max();
                int randomValue = min + random.nextInt(max - min);
                field.setAccessible(true);
                field.set(t, randomValue);
            }
        }
    }

}
