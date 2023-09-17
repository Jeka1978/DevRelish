package com.epam.devrelish;

import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class DeprecatedAnnotationMethodsInformerObjectConfigurator implements ProxyConfigurator {

    private Map<Method, Integer> deprecatedMethodsUsageCount = new HashMap<>();

    @Override
    @SneakyThrows
    public Object wrapWithProxyIfNeeded(Object t) {
        Class<?> implClass = t.getClass();
        for (Method method : implClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Deprecated.class)) {
                if (implClass.getInterfaces().length == 0) {
                    return creatyByteBodyProxy(t, implClass);
                } else {
                    return createDynamicProxy(t, implClass, method);
                }
            }
        }
        return t;
    }

    private Object createDynamicProxy(Object t, Class<?> implClass, Method method) {
        Object proxy = Proxy.newProxyInstance(implClass.getClassLoader(),
                implClass.getInterfaces(),
                (proxy1, method1, args) -> {
                    if (implClass.getMethod(method1.getName(), method1.getParameterTypes()).isAnnotationPresent(Deprecated.class)) {
                        deprecatedMethodsUsageCount.merge(method1, 1, Integer::sum);
                        System.out.println("Warning: The method " + method1.getName() + " in class " + implClass.getName() + " is deprecated. It was called " + deprecatedMethodsUsageCount.get(method1) + " times.");
                    }
                    return method1.invoke(t, args);
                });

        System.out.println("Warning: The method " + method.getName() + " in class " + implClass.getName() + " is deprecated.");
        return proxy;
    }

    private Object creatyByteBodyProxy(Object t, Class<?> implClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return new ByteBuddy()
                .subclass(implClass)
                .method(ElementMatchers.isAnnotatedWith(Deprecated.class))
                .intercept(InvocationHandlerAdapter.of(new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        deprecatedMethodsUsageCount.merge(method, 1, Integer::sum);
                        System.out.println("Warning: The method " + method.getName() + " in class " + implClass.getName() + " is deprecated. It was called " + deprecatedMethodsUsageCount.get(method) + " times.");
                        return method.invoke(t, args);
                    }
                }))
                .make()
                .load(implClass.getClassLoader())
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();
    }

    public Map<Method, Integer> getDeprecatedMethodsUsageCount() {
        return deprecatedMethodsUsageCount;
    }
}


