package com.epam.devrelish;

import lombok.SneakyThrows;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

public class DeprecatedInvocationHandler implements InvocationHandler {
    private final Object originalObject;

    private final AtomicInteger deprecatedMethodInvocationCount = new AtomicInteger(0);

    @SneakyThrows
    public DeprecatedInvocationHandler(Object originalObject) {
        this.originalObject = originalObject;
    }

    @Override
    @SneakyThrows
    public Object invoke(Object proxy, Method method, Object[] args)  {
        Method originalMethod = originalObject.getClass().getMethod(method.getName(), method.getParameterTypes());
        if (originalMethod.isAnnotationPresent(Deprecated.class)) {
            deprecatedMethodInvocationCount.incrementAndGet();
            System.out.println(method.getName()+" is deprecated total invocations = "+getDeprecatedMethodInvocationCount());
        }
        return method.invoke(originalObject, args);
    }

    public int getDeprecatedMethodInvocationCount() {
        return deprecatedMethodInvocationCount.get();
    }
}
