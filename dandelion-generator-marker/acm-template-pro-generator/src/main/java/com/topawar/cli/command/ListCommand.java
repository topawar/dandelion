package com.topawar.cli.command;

import cn.hutool.core.io.FileUtil;
import picocli.CommandLine;

import java.io.File;
import java.util.List;

/**
 * @author topawar
 */
@CommandLine.Command(name = "list",description = "查看生成的文件列表",mixinStandardHelpOptions = true)
public class ListCommand implements Runnable{
    @Override
    public void run() {
        String parentPath = System.getProperty("user.dir");
        File parentFile = new File(parentPath).getParentFile();
        String inputPath = new File(parentFile, ".source/acm-template-pro").getAbsolutePath();
        List<File> files = FileUtil.loopFiles(inputPath);
        for (File file : files) {
            System.out.println(file);
        }
    }
}
