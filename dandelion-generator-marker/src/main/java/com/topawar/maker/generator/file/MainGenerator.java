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
        String projectPath = System.getProperty("user.dir");
        File parentFile = new File(projectPath).getParentFile();
        File inputPath = new File(parentFile, "dandelion-generator-demo-projects/acm-template");
        String outputPath = projectPath;
        StaticFileGenerator.copyFilesByHutool(inputPath.getAbsolutePath(), outputPath);
        String inputDynamicGeneratorPath = projectPath + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
        String outputDynamicGeneratorPath = outputPath + File.separator + "acm-template/src/com/yupi/acm/MainTemplate.java";
        DynamicFileGenerator.doGenerate(inputDynamicGeneratorPath,outputDynamicGeneratorPath, dataModel);
    }

    public static void main(String[] args) throws TemplateException, IOException {
        //初始化meta对象
        Meta meta = MetaManager.getMetaObject();
        //获取生成文件的路径
        String projectPath = System.getProperty("user.dir");
        String outputPath=projectPath+File.separator+"generated"+File.separator+meta.getName();
        //不存在则创建
        if (!FileUtil.exist(outputPath)){
            FileUtil.mkdir(outputPath);
        }
        //读取resource
        ClassPathResource classPathResource = new ClassPathResource("");
        String resourceAbsolutePath = classPathResource.getAbsolutePath();

        //java包路径
        String basePackage = meta.getBasePackage();
        String outputBasePackagePath = StrUtil.join("/", StrUtil.split(basePackage,"."));
        String outputBaseJavaPackagePath=outputPath+File.separator+"src/main/java"+File.separator+outputBasePackagePath;

        String inputFilePath;
        String outputFilePath;

        //生成数据模型
        inputFilePath=resourceAbsolutePath+File.separator+"templates/java/model/DataModel.java.ftl";
        outputFilePath=outputBaseJavaPackagePath+"/model/DataModel.java";
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,meta);
    }
}
