package com.epam.devrelish;

import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class InjectByTypeAnnotationConfigurator implements ObjectConfigurator {

    @SneakyThrows
    @Override
    public void configure(Object t) {
        for (Field field : t.getClass().getDeclaredFields()) {
            InjectByType annotation = field.getAnnotation(InjectByType.class);
            if (annotation != null) {
                Object valueToInject = ObjectFactory.getInstance().createObject(field.getType());
                field.setAccessible(true);
                field.set(t, valueToInject);
            }
        }
    }
}
