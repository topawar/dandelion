package ${basePackage}.generator;

import cn.hutool.core.io.FileUtil;

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
