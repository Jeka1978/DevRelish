package com.epam.devrelish;

public class RandomMetricsCollector implements MetricsCollector {
    @Override
    public double collect(String devRelActivity) {
        return Math.random();
    }
}
