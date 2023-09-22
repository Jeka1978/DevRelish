package com.epam.devrelish;

import lombok.SneakyThrows;

import java.lang.reflect.*;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.SneakyThrows;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

public class DeprecatedAnnotationMethodsInformerObjectConfigurator implements ProxyConfigurator {

    @Override
    @SneakyThrows
    public Object configure(Object obj) {
        long deprecatedMethodCount = countDeprecatedMethods(obj);

        if (deprecatedMethodCount == 0) {
            return obj;
        }

        Object proxyInstance;
        if (obj.getClass().getInterfaces().length == 0) {
            proxyInstance = createByteBuddyProxy(obj);
        } else {
            proxyInstance = createDynamicProxy(obj);
        }

        return proxyInstance;
    }

    private Object createDynamicProxy(Object obj) {
        return Proxy.newProxyInstance(
                obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                new DeprecatedInvocationHandler(obj)
        );
    }

    private Object createByteBuddyProxy(Object obj) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Object proxyInstance;
        proxyInstance = new ByteBuddy()
                .subclass(obj.getClass())
                .method(ElementMatchers.isAnnotatedWith(Deprecated.class))
                .intercept(InvocationHandlerAdapter.of(new DeprecatedInvocationHandler(obj)))
                .make()
                .load(obj.getClass().getClassLoader())
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();
        return proxyInstance;
    }

    private long countDeprecatedMethods(Object obj) {
        return java.util.Arrays.stream(obj.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(Deprecated.class))
                .count();
    }




}



