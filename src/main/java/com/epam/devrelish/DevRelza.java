package com.epam.devrelish;


import jakarta.annotation.PostConstruct;

public class DevRelza {
    @InjectByType
    private MetricsCollector metricCollector;
    @InjectByType
    private DevRelActionProducer producer;
    @InjectByType
    private DevRelAnalyzer analyzer;

    @PostConstruct
    public void init() {
        System.out.println(metricCollector.getClass());
    }

    public void executeDevRelStrategy() {

        String devRelActivity = analyzer.findMostCriticalActivity();

        double howMuch = metricCollector.collect(devRelActivity);

        producer.produce(devRelActivity);

        System.out.println("improved by: " + (metricCollector.collect(devRelActivity) - howMuch) * 100 + " DevDollars");

    }

}
