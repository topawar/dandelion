package com.topawar.maker.generator;

import java.io.*;

/**
 * @author topawar
 */
public class JarGenerator {
    public static void doGenerate(String projectDir) throws IOException, InterruptedException {
        //清理构建并打包
        String winMavenCommand = "mvn.cmd clean package -DskipTests=true";
        String otherMavenCommand = "mvn clean package -DskipTests=true";

        String mvnCommand = otherMavenCommand;
        // 这里一定要拆分
        ProcessBuilder processBuilder = new ProcessBuilder(mvnCommand.split(" "));
        processBuilder.directory(new File(projectDir));

        Process process = processBuilder.start();

        InputStream processInputStream = process.getInputStream();
        //读取日志
        BufferedReader reader = new BufferedReader(new InputStreamReader(processInputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        //等待命令完成
        int exitCode = process.waitFor();
        System.out.println("命令执行完成：退出码："+exitCode);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        doGenerate("D:\\Project\\ideaProject\\dandelion\\dandelion-generator-marker\\generated\\acm-template-pro-generator");
    }
}
