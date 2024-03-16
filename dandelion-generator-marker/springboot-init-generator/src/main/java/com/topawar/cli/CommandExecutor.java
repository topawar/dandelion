package com.topawar.cli;

import com.topawar.cli.command.ConfigCommand;
import com.topawar.cli.command.GenerateCommand;
import com.topawar.cli.command.ListCommand;
import com.topawar.cli.command.JsonGeneratorCommand;
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
                .addSubcommand(new ListCommand())
                .addSubcommand(new JsonGeneratorCommand());
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
