package com.epam.devrelish;


public class DevRelishApplication {

    public static void main(String[] args) {
        DevRelza devRelza = ObjectFactory.getInstance().createObject(DevRelza.class);
        devRelza.executeDevRelStrategy();
        ObjectFactory.getInstance().createObject(MyTest.class).test();
    }

}
