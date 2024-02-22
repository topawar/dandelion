package ${basePackage}.generator;

import ${basePackage}.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
* @author topawar
*/
public class MainGenerator {

public static void doGenerate(DataModel dataModel) throws TemplateException, IOException {
String inputRootPath ="${fileConfig.inputRootPath}";
String outputRootPath ="${fileConfig.outputRootPath}";
String inputPath;
String outputPath;
<#list fileConfig.files as file>
     inputPath = new File(inputRootPath, "${file.inputPath}").getAbsolutePath();
     outputPath = new File(outputRootPath, "${file.outputPath}").getAbsolutePath();
    <#if file.generateType=="dynamic">
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
    <#else>
        //生成静态文件
        StaticFileGenerator.copyFilesByHutool(inputPath, outputPath);
    </#if>
</#list>
}
}
