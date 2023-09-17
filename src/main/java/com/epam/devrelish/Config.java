package com.epam.devrelish;

public interface Config {
    <T> Class<? extends T> getImplementation(Class<T> interfaceType);

    org.reflections.Reflections getScanner();
}
