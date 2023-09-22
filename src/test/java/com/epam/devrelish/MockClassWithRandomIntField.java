package com.epam.devrelish;

public class MockClassWithRandomIntField {
    @InjectRandomInt(min = 3, max = 7)
    private int randomIntField;

    public int getRandomIntField() {
        return randomIntField;
    }
}

