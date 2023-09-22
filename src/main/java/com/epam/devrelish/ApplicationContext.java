package com.epam.devrelish;

import lombok.Getter;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {

    private final ObjectFactory objectFactory;
    @Getter
    private final Config config;
    private final Map<Class<?>, Object> singletonCache;

    public ApplicationContext(String packagesToScan) {
        this.config = new Config(packagesToScan);
        this.objectFactory = new ObjectFactory(this);
        this.singletonCache = new HashMap<>();
    }

    @SneakyThrows
    public <T> T getObject(Class<T> type) {
        // Step 1: Check if the object is already in the cache
        if (singletonCache.containsKey(type)) {
            return (T) singletonCache.get(type);
        }

        // Step 2: Check if it is an interface and align with the implementation class
        Class<? extends T> implementationType = type.isInterface() ? config.getImplementation(type) : type;

        // Step 3: Create the object using the ObjectFactory
        T obj = objectFactory.createObject(implementationType);

        // Step 4: Cache the object if it's marked as @Singleton
        if (implementationType.isAnnotationPresent(Singleton.class)) {
            singletonCache.put(type, obj);
        }

        return obj;
    }
}

