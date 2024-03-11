package com.topawar.maker.generator.file;

/**
 * @author topawar
 */
public class ZipGenerator extends GenerateTemplate{
    @Override
    protected String buildDist(String outputPath, String sourceCopyDestPath, String jarPath, String shellOutputFilePath) {
        String buildDistPath = super.buildDist(outputPath, sourceCopyDestPath, jarPath, shellOutputFilePath);
        return super.buildZip(buildDistPath);
    }
}
