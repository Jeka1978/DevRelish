package com.epam.devrelish;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JavaConfig implements Config {
    private Map<Class<?>, Class<?>> interfaceToImplementationMap = new HashMap<>();
    @Getter
    private Reflections scanner = new Reflections("com.epam.devrelish");

    @SneakyThrows
    public JavaConfig() {
        File jsonFile = new File("config.json");

        Map<String, String> mappings = new ObjectMapper().readValue(jsonFile, Map.class);

        interfaceToImplementationMap = mappings.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> getClassByName(entry.getKey()),
                        entry -> getClassByName(entry.getValue())
                ));
    }

    @SneakyThrows
    private static Class<?> getClassByName(String name) {
        return Class.forName(name);
    }

    public <T> Class<? extends T> getImplementation(Class<T> interfaceType) {
        return (Class<? extends T>) interfaceToImplementationMap.computeIfAbsent(interfaceType, this::findImplementationInClasspath);
    }

    private <T> Class<?> findImplementationInClasspath(Class<T> interfaceType) {
        Set<Class<? extends T>> subTypes = scanner.getSubTypesOf(interfaceType);

        if (subTypes.size() == 1) {
            return subTypes.iterator().next();
        } else if (subTypes.size() > 1) {
            throw new IllegalArgumentException(
                    "Multiple implementations found for interface " + interfaceType.getName() + ". " +
                            "Please register a specific implementation in the config.");
        } else {
            throw new IllegalArgumentException(
                    "No implementations found for interface " + interfaceType.getName() + " in the classpath. " +
                            "Please ensure a concrete implementation exists and is in the correct package, or register " +
                            "an implementation in the config.");
        }
    }
}
