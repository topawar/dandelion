package com.topawar.cli.example.pattern;

/**
 * @author topawar
 */
public class Client {
    public static void main(String[] args) {
        Device tv1 = new Device("tv1");
        Device tv2 = new Device("tv2");

        TurnOffCommand turnOffCommand = new TurnOffCommand(tv1);
        TurnOnCommand turnOnCommand = new TurnOnCommand(tv2);

        RemoteControl remoteControl = new RemoteControl();
        remoteControl.setCommand(turnOffCommand);
        remoteControl.processButton();

        remoteControl.setCommand(turnOnCommand);
        remoteControl.processButton();
    }
}
