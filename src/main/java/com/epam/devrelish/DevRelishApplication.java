package com.epam.devrelish;


public class DevRelishApplication {

    public static void main(String[] args) {
        ApplicationContext context = new ApplicationContext("com.epam");
        context.getObject(DevRelza.class).executeDevRelStrategy();
        context.getObject(WithoutInterfaces.class).veryOld();
        context.getObject(WithoutInterfaces.class).veryOld();
        context.getObject(WithoutInterfaces.class).veryOld();
    }

}
