package com.topawar.generator;

import com.topawar.model.MainTemplateConfig;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * @author topawar
 */
public class MainGenerator {
    public static void main(String[] args) throws TemplateException, IOException {
        String projectPath = System.getProperty("user.dir");
        File parentFile = new File(projectPath).getParentFile();
        File inputPath = new File(parentFile, "dandelion-generator-demo-projects/acm-template");
        String outputPath = projectPath;
        StaticGenerator.copyFilesByHutool(inputPath.getAbsolutePath(), outputPath);
        String inputDynamicGeneratorPath = projectPath + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
        String outputDynamicGeneratorPath = outputPath + File.separator + "acm-template/src/com/yupi/acm/MainTemplate.java";
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        mainTemplateConfig.setAuthor("topawar222");
        mainTemplateConfig.setLoop(false);
        mainTemplateConfig.setOutputText("总数");
        DynamicGenerator.doGenerate(inputDynamicGeneratorPath,outputDynamicGeneratorPath,mainTemplateConfig);
    }
}
