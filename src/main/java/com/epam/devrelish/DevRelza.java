package com.epam.devrelish;

import javax.annotation.PostConstruct;

public class DevRelza {
    @InjectByType
    private MetricsCollector metricCollector;
    @InjectByType
    private DevRelActionProducer producer;
    @InjectByType
    private DevRelAnalyzer analyzer;
    @PostConstruct
    public void init() {
        System.out.println(analyzer);
    }

    public void executeDevRelStrategy() {

        String devRelActivity = analyzer.findMostCriticalActivity();

        double howMuch = metricCollector.collect(devRelActivity);

        producer.produce(devRelActivity);

        System.out.println(metricCollector.collect(devRelActivity) - howMuch);

    }

}
