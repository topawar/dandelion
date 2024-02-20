package com.topawar.maker;

import com.topawar.maker.cli.CommandExecutor;

/**
 * @author topawar
 */
public class Main {
    public static void main(String[] args) {
//        args=new String[]{"generate","-a","-l","-t"};
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.doExecute(args);
    }
}
