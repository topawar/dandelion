package com.topawar.maker.generator.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.topawar.maker.meta.Meta;
import com.topawar.maker.meta.MetaManager;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * @author topawar
 */
public class GenerateTemplate {
    public void doGenerate() throws TemplateException, IOException, InterruptedException {
        //初始化meta对象
        Meta meta = MetaManager.getMetaObject();
        //获取生成文件的路径
        String projectPath = System.getProperty("user.dir");
        String outputPath = projectPath + File.separator + File.separator + meta.getName();
        //不存在则创建
        if (!FileUtil.exist(outputPath)) {
            FileUtil.mkdir(outputPath);
        }
        //复制文件到.source文件夹下
        String sourceCopyDestPath = copySource(meta, outputPath);
        //生成代码文件
        generateCode(meta, outputPath);
        //封装 jar包
        String jarPath = buildJar(meta,outputPath);
        //生成脚本文件
        String shellOutputFilePath = buildScript(outputPath, jarPath);

        //生成产物包
        buildDist(outputPath, sourceCopyDestPath, jarPath, shellOutputFilePath);
    }

    protected void buildDist(String outputPath, String sourceCopyDestPath, String jarPath, String shellOutputFilePath){
        String distOutputPath = outputPath + "-dist";
        // 拷贝 jar 包
        String targetAbsolutePath = distOutputPath + File.separator + "target";
        FileUtil.mkdir(targetAbsolutePath);
        String jarAbsolutePath = outputPath + File.separator + jarPath;
        FileUtil.copy(jarAbsolutePath, targetAbsolutePath, true);
        // 拷贝脚本文件
        FileUtil.copy(shellOutputFilePath, distOutputPath, true);
        FileUtil.copy(shellOutputFilePath+".bat", distOutputPath, true);
        // 拷贝源模板文件
        FileUtil.copy(sourceCopyDestPath, distOutputPath, true);
    }

    protected String buildScript(String outputPath, String jarPath) {
        String shellOutputFilePath = outputPath + File.separator + "exec";
        ScriptGenerator.doGenerator(shellOutputFilePath, jarPath);
        return shellOutputFilePath;
    }

    protected String buildJar(Meta meta,String outputPath) throws IOException, InterruptedException {
        JarGenerator.doGenerate(outputPath);
        String jarName = String.format("%s-%s-SNAPSHOT-jar-with-dependencies.jar", meta.getName(), meta.getVersion());
        String jarPath = "target/" + jarName;
        return jarPath;
    }

    protected void generateCode(Meta meta, String outputPath) throws IOException, TemplateException {
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

        //生成REDAME.MD 文件
//        inputFilePath = resourceAbsolutePath + File.separator + "templates/README.MD.ftl";
//        outputFilePath = outputPath + File.separator + "README.MD";
//        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
    }

    protected String copySource(Meta meta, String outputPath) {
        String sourcePath = meta.getFileConfig().getSourceRootPath();
        String sourceCopyDestPath = outputPath + File.separator + ".source";
        FileUtil.copy(sourcePath, sourceCopyDestPath, false);
        return sourceCopyDestPath;
    }

}
