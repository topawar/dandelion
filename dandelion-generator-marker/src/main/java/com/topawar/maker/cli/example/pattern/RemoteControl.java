package com.topawar.maker.cli.example.pattern;

/**
 * @author topawar
 */
public class RemoteControl {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void processButton(){
        command.execute();
    }
}
