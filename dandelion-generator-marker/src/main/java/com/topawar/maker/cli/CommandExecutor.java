package com.topawar.maker.cli;

import com.topawar.maker.cli.command.ConfigCommand;
import com.topawar.maker.cli.command.GenerateCommand;
import com.topawar.maker.cli.command.ListCommand;
import picocli.CommandLine;

/**
 * @author topawar
 */
@CommandLine.Command(name = "dandelion",mixinStandardHelpOptions = false)
public class CommandExecutor implements Runnable {

    private final CommandLine commandLine;

    {
        commandLine = new CommandLine(this)
                .addSubcommand(new ConfigCommand())
                .addSubcommand(new GenerateCommand())
                .addSubcommand(new ListCommand());
    }

    @Override
    public void run() {
        System.out.println("输入--help获取提示");
    }

    /**
     * 父命令入口
     * @param args
     */
    public Integer doExecute(String[] args) {
        return  commandLine.execute(args);
    }
}
