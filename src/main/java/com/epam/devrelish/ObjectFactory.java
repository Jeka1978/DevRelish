package com.epam.devrelish;

import lombok.SneakyThrows;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ObjectFactory {

    private static final ObjectFactory INSTANCE = new ObjectFactory();
    private Config config;  // Assuming Config is a class that holds the mapping between interfaces and their implementations
    private List<ObjectConfigurator> configurators = new ArrayList<>();
    private List<ProxyConfigurator> proxyConfigurators = new ArrayList<>();

    @SneakyThrows
    private ObjectFactory() {
        // Initialize or load the Config object
        config = new JavaConfig();
        getConfigurators(configurators,ObjectConfigurator.class);
        getConfigurators(proxyConfigurators,ProxyConfigurator.class);
        System.out.println();

    }

    private void getConfigurators(List configurators, Class type) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Set<Class<?>> configuratorClasses = config.getScanner().getSubTypesOf(type);

        for (Class<?> configuratorClass : configuratorClasses) {
            configurators.add(configuratorClass.getDeclaredConstructor().newInstance());
        }
    }

    public static ObjectFactory getInstance() {
        return INSTANCE;
    }

    @SneakyThrows
    public <T> T createObject(Class<T> type) {
        T t = resolveImpl(type);
        configure(t);
        callPostConstructMethod(t);
        t = handleProxy(t);
        return t;
    }

    private <T> T handleProxy(T t) {
        for (ProxyConfigurator proxyConfigurator : proxyConfigurators) {
            t = (T) proxyConfigurator.wrapWithProxyIfNeeded(t);
        }
        return t;
    }

    private <T> void configure(T t) {
        for (ObjectConfigurator configurator : configurators) {
            configurator.configure(t);
        }
    }

    private <T> T resolveImpl(Class<T> type) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        T t;
        if (type.isInterface()) {
            Class<? extends T> implClass = config.getImplementation(type);
            t = implClass.getDeclaredConstructor().newInstance();
        } else {
            t = type.getDeclaredConstructor().newInstance();
        }
        return t;
    }

    @SneakyThrows
    private <T> void callPostConstructMethod(T t) {
        for (Method method : t.getClass().getMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                method.invoke(t);
            }
        }
    }


}

