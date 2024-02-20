package com.topawar.maker.cli.example.pattern;

/**
 * @author topawar
 */
public class Device {
    private String name;

    public Device(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    void turnOff(){
        System.out.println(name+"关闭");
    }

    void turnOn(){
        System.out.println(name+"打开");
    }
}
