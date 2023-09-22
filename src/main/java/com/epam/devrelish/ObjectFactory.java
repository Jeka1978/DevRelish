package com.epam.devrelish;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectFactory {


    private ApplicationContext context;
    private List<ObjectConfigurator> configurators;
    private List<ProxyConfigurator> proxyConfigurators;

    protected ObjectFactory(ApplicationContext context) {
        this.context=context;
        // Initialize the list of ObjectConfigurators
        configurators = context.getConfig().getImplementations(ObjectConfigurator.class).stream()
                .map(this::createConfiguratorInstance)
                .collect(Collectors.toList());

        // Initialize the list of ProxyConfigurators
        proxyConfigurators = context.getConfig().getImplementations(ProxyConfigurator.class).stream()
                .map(this::createConfiguratorInstance)
                .collect(Collectors.toList());
    }



    @SneakyThrows
    public <T> T createObject(Class<T> type) {
        T obj = type.getDeclaredConstructor().newInstance();


        configureObject(obj);
        invokePostConstruct(obj);

        return wrapWithProxyIfNeeded(obj);
    }

    @SneakyThrows
    private <T> T createConfiguratorInstance(Class<? extends T> type) {
        return type.getDeclaredConstructor().newInstance();
    }

    private void configureObject(Object obj) {
        for (ObjectConfigurator configurator : configurators) {
            configurator.configure(obj,context);
        }
    }

    @SneakyThrows
    private void invokePostConstruct(Object obj) {
        for (Method method : obj.getClass().getMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                method.invoke(obj);
            }
        }
    }

    private <T> T wrapWithProxyIfNeeded(T obj) {
        for (ProxyConfigurator proxyConfigurator : proxyConfigurators) {
            obj = (T) proxyConfigurator.configure(obj);
        }
        return obj;
    }
}






