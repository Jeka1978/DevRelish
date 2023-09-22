package com.epam.devrelish;

import lombok.SneakyThrows;

public interface ObjectConfigurator {
    @SneakyThrows
    void configure(Object obj,ApplicationContext applicationContext);
}
