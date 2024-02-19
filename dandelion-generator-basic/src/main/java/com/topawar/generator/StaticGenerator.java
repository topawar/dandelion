package com.topawar.generator;

import cn.hutool.core.io.FileUtil;

import java.io.File;

/**
 * @author topawar
 */
public class StaticGenerator {


    public static void copyFilesByHutool(String inputPath, String outputPath) {
        FileUtil.copy(inputPath, outputPath, false);
    }

    public static void main(String[] args) {
        String projectDir = System.getProperty("user.dir");
        File parentFile = new File(projectDir).getParentFile();
        String inputPath = new File(parentFile, "dandelion-generator-demo-projects/acm-template").getAbsoluteFile().getAbsolutePath();
        copyFilesByHutool(inputPath,projectDir);
    }
}
