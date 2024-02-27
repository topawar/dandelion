package ${basePackage}.generator;

import ${basePackage}.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

<#macro generateFile indent fileInfo>
    ${indent}inputPath = new File(inputRootPath, "${fileInfo.inputPath}").getAbsolutePath();
    ${indent}outputPath = new File(outputRootPath, "${fileInfo.outputPath}").getAbsolutePath();
    <#if fileInfo.generateType=="dynamic">
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
    <#else>
        //生成静态文件
        StaticFileGenerator.copyFilesByHutool(inputPath, outputPath);
    </#if>
</#macro>

/**
* @author topawar
*/
public class MainGenerator {

public static void doGenerate(DataModel dataModel) throws TemplateException, IOException {
    String inputRootPath ="${fileConfig.inputRootPath}";
    String outputRootPath ="${fileConfig.outputRootPath}";
    String inputPath;
    String outputPath;
    <#list modelConfig.models as model>
        <#--        有分组-->
        <#if model.groupKey??>
        <#list model.models as modelInfo>
            ${modelInfo.type} ${modelInfo.fieldName}=dataModel.${model.groupKey}.${modelInfo.fieldName};
        </#list>
        <#else>
        ${model.type} ${model.fieldName}= dataModel.${model.fieldName};
        </#if>
    </#list>

    <#list fileConfig.files as file>
     <#if file.groupKey??>
        <#if file.condition??>
        if(${file.condition}){
        <#list file.files as fileInfo>
           <@generateFile indent="         " fileInfo=fileInfo></@generateFile>
        </#list>
       }
     <#else>
         <#list file.files as fileInfo>
             <@generateFile indent="         " fileInfo=fileInfo></@generateFile>
         </#list>
         </#if>
         <#else>
     <#if file.condition??>
        if(${file.condition}){
         <@generateFile indent="         " fileInfo=file></@generateFile>
         }
    <#else>
        <@generateFile indent="         " fileInfo=file></@generateFile>
     </#if>
     </#if>
    </#list>
}
}
