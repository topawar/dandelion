package com.topawar.generator;

import com.topawar.model.MainTemplateConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author topawar
 */
public class DynamicGenerator {
    public static void main(String[] args) throws IOException, TemplateException {
        String inputPath="src/main/resources/templates/MainTemplate.java.ftl";
        String outputPath="MainTemplate.java";
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        mainTemplateConfig.setAuthor("topawar2");
        mainTemplateConfig.setLoop(true);
        mainTemplateConfig.setOutputText("求和结果：");
        doGenerate(inputPath,outputPath,mainTemplateConfig);
    }

    public static void doGenerate(String inputPath,String outputPath, Object model) throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
        File parentFile = new File(inputPath).getParentFile();
        configuration.setDirectoryForTemplateLoading(parentFile);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setNumberFormat("0.######");
        Template template = configuration.getTemplate(new File(inputPath).getName());
        FileWriter out = new FileWriter(outputPath);
        template.process(model, out);
        out.close();
    }
}
