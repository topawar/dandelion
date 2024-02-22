package com.topawar.maker.generator.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.topawar.maker.meta.Meta;
import com.topawar.maker.meta.MetaManager;
import com.topawar.maker.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * @author topawar
 */
public class MainGenerator {

    public static void doGenerate(DataModel dataModel) throws TemplateException, IOException {
        String inputRootPath = "D:\\Project\\ideaProject\\dandelion\\dandelion-generator-demo-projects\\acm-template-pro";
        String outputRootPath = "D:\\Project\\ideaProject\\dandelion\\acm-template-pro";
        //动态生成文件
        String inputPath = new File(inputRootPath, "src/com/yupi/acm/MainTemplate.java.ftl").getAbsolutePath();
        String outputPath = new File(outputRootPath, "src/com/yupi/acm/MainTemplate.java").getAbsolutePath();
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
        //生成静态文件
        inputPath = new File(inputRootPath, ".gitignore").getAbsolutePath();
        outputPath = new File(outputRootPath, ".gitignore").getAbsolutePath();
        StaticFileGenerator.copyFilesByHutool(inputPath, outputPath);
        inputPath = new File(inputRootPath, "README.MD").getAbsolutePath();
        outputPath = new File(outputRootPath, "README.MD").getAbsolutePath();
        StaticFileGenerator.copyFilesByHutool(inputPath, outputPath);
    }

    public static void main(String[] args) throws TemplateException, IOException {
        //初始化meta对象
        Meta meta = MetaManager.getMetaObject();
        //获取生成文件的路径
        String projectPath = System.getProperty("user.dir");
        String outputPath = projectPath + File.separator + "generated" + File.separator + meta.getName();
        //不存在则创建
        if (!FileUtil.exist(outputPath)) {
            FileUtil.mkdir(outputPath);
        }
        //读取resource
        ClassPathResource classPathResource = new ClassPathResource("");
        String resourceAbsolutePath = classPathResource.getAbsolutePath();

        //java包路径
        String basePackage = meta.getBasePackage();
        String outputBasePackagePath = StrUtil.join("/", StrUtil.split(basePackage, "."));
        String outputBaseJavaPackagePath = outputPath + File.separator + "src/main/java" + File.separator + outputBasePackagePath;

        String inputFilePath;
        String outputFilePath;

        //生成数据模型
        inputFilePath = resourceAbsolutePath + File.separator + "templates/java/model/DataModel.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/model/DataModel.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
        //生成命令文件
        inputFilePath = resourceAbsolutePath + File.separator + "templates/java/cli/command/ConfigCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/ConfigCommand.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
        inputFilePath = resourceAbsolutePath + File.separator + "templates/java/cli/command/GenerateCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/GenerateCommand.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
        inputFilePath = resourceAbsolutePath + File.separator + "templates/java/cli/command/ListCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/ListCommand.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
        inputFilePath = resourceAbsolutePath + File.separator + "templates/java/cli/CommandExecutor.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/CommandExecutor.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
        //生成动静生成文件
        inputFilePath = resourceAbsolutePath + File.separator + "templates/java/generate/DynamicFileGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/DynamicFileGenerator.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
        inputFilePath = resourceAbsolutePath + File.separator + "templates/java/generate/MainGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/MainGenerator.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
        inputFilePath = resourceAbsolutePath + File.separator + "templates/java/generate/StaticFileGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/StaticFileGenerator.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        //生成程序入口
        inputFilePath = resourceAbsolutePath + File.separator + "templates/java/Main.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/Main.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        //动态生成pom文件
        inputFilePath = resourceAbsolutePath + File.separator + "templates/pom.xml.ftl";
        outputFilePath = outputPath + File.separator + "pom.xml";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        //生成脚本文件
        String shellOutputFilePath = outputPath + File.separator + "exec";
        //acm-template-pro-generator-1.0-SNAPSHOT.jar
        String jarName=String.format("%s-%s-SNAPSHOT-jar-with-dependencies.jar",meta.getName(),meta.getVersion());
        String jarPath="target/"+jarName;
        ScriptGenerator.doGenerator(shellOutputFilePath,jarPath);
    }
}
