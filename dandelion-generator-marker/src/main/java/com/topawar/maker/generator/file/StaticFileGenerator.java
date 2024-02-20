package com.topawar.maker.generator.file;

import cn.hutool.core.io.FileUtil;

import java.io.File;

/**
 * @author topawar
 */
public class StaticFileGenerator {


    /**
     *
     * @param inputPath
     * @param outputPath
     */
    public static void copyFilesByHutool(String inputPath, String outputPath) {
        FileUtil.copy(inputPath, outputPath, false);
    }

}
