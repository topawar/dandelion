package com.topawar.maker.cli.example;

import picocli.CommandLine;

/**
 * @author topawar
 */
@CommandLine.Command(name = "main",mixinStandardHelpOptions = true)
public class SubCommandExample implements Runnable{

    @Override
    public void run() {
        System.out.println("主方法");
    }

    @CommandLine.Command(name = "add",description = "增加",mixinStandardHelpOptions = true)
    static class AddCommand implements Runnable{
        @Override
        public void run() {
            System.out.println("add方法");
        }
    }

    @CommandLine.Command(name = "delete",description = "删除",mixinStandardHelpOptions = true)
    static class DeleteCommand implements Runnable{
        @Override
        public void run() {
            System.out.println("delete");
        }
    }

    @CommandLine.Command(name = "update",description = "修改",mixinStandardHelpOptions = true)
    static class UpdateCommand implements Runnable{
        @Override
        public void run() {
            System.out.println("update");
        }
    }

    public static void main(String[] args) {
        int execute = new CommandLine(new SubCommandExample())
                .addSubcommand(new AddCommand())
                .addSubcommand(new DeleteCommand())
                .addSubcommand(new UpdateCommand())
                .execute(args);
        System.exit(execute);
    }
}
