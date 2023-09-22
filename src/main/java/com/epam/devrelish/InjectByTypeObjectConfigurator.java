package com.epam.devrelish;

import lombok.SneakyThrows;

import java.lang.reflect.Field;

public class InjectByTypeObjectConfigurator implements ObjectConfigurator {


    @SneakyThrows
    @Override
    public void configure(Object t,ApplicationContext applicationContext) {
        for (Field field : t.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(InjectByType.class)) {
                field.setAccessible(true);
                Object objectToInject = applicationContext.getObject(field.getType());
                field.set(t, objectToInject);
            }
        }
    }
}
