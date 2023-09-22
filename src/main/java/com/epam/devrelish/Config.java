package com.epam.devrelish;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.SneakyThrows;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reflections.Reflections;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.util.*;
import java.util.stream.Collectors;

public class Config {

    private final Map<Class<?>, Class<?>> interfaceToImplementationMap;
    private final Reflections reflections;

    public Config(String packagesToScan) {
        this.interfaceToImplementationMap = new JsonToMapReader().readJsonToMap("config.json");
        this.reflections = new Reflections(packagesToScan);
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <T> Class<? extends T> getImplementation(Class<T> interfaceType) {
        if (!interfaceToImplementationMap.containsKey(interfaceType)) {
            Set<Class<? extends T>> classes = reflections.getSubTypesOf(interfaceType);

            if (classes.isEmpty()) {
                throw new IllegalArgumentException("No implementation found for " + interfaceType.getName());
            }

            if (classes.size() > 1) {
                String implNames = classes.stream().map(Class::getName).collect(Collectors.joining(","));
                throw new IllegalArgumentException("Multiple implementations found for " + interfaceType.getName() + ": " + implNames);
            }

            interfaceToImplementationMap.put(interfaceType, classes.iterator().next());
        }

        return (Class<? extends T>) interfaceToImplementationMap.get(interfaceType);
    }

    public <T> List<Class<? extends T>> getImplementations(Class<T> interfaceType) {
        Set<Class<? extends T>> classes = reflections.getSubTypesOf(interfaceType);
        return new ArrayList<>(classes);
    }
}






