package com.epam.devrelish;

public class OptimisticMetricsCollector implements MetricsCollector {
    //should be random in range 1 to 4
    @InjectRandomInt(min = 100, max = 200)
    private int initialMetric = 1;

    //should be random in range 2 to 5
    private int delta = 2;

    private int counter;

    @Override
    @Deprecated
    public double collect(String devRelActivity) {
        counter++;
        if (counter == 1) {
            return initialMetric;
        }
        return counter * initialMetric * delta;
    }
}
