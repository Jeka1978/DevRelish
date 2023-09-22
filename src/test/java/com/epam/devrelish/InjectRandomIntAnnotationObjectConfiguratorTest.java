package com.epam.devrelish;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InjectRandomIntAnnotationObjectConfiguratorTest {

    private InjectRandomIntAnnotationObjectConfigurator configurator;

    @BeforeEach
    public void setUp() {
        configurator = new InjectRandomIntAnnotationObjectConfigurator();
    }

    @Test
    public void testConfigure() {
        MockClassWithRandomIntField mockObject = new MockClassWithRandomIntField();
        configurator.configure(mockObject);

        int value = mockObject.getRandomIntField();

        // Testing that the random integer falls within the specified min and max range.
        assertTrue(value >= 3 && value < 7);
    }
}
