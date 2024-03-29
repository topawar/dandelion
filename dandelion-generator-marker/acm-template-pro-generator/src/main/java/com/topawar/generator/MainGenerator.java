package com.topawar.generator;

import com.topawar.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;


/**
* @author topawar
*/
public class MainGenerator {

public static void doGenerate(DataModel dataModel) throws TemplateException, IOException {
    String inputRootPath =".source/acm-template-pro";
    String outputRootPath ="generated";
    String inputPath;
    String outputPath;
        boolean loop= dataModel.loop;
        boolean needGit= dataModel.needGit;
            String author=dataModel.mainTemplate.author;
            String outputText=dataModel.mainTemplate.outputText;

             inputPath = new File(inputRootPath, "src/com/yupi/acm/MainTemplate.java.ftl").getAbsolutePath();
             outputPath = new File(outputRootPath, "src/com/yupi/acm/MainTemplate.java").getAbsolutePath();
        //动态生成文件
        DynamicFileGenerator.doGenerate(inputPath, outputPath, dataModel);
        if(needGit){
             inputPath = new File(inputRootPath, ".gitignore").getAbsolutePath();
             outputPath = new File(outputRootPath, ".gitignore").getAbsolutePath();
        //生成静态文件
        StaticFileGenerator.copyFilesByHutool(inputPath, outputPath);
             inputPath = new File(inputRootPath, "README.md").getAbsolutePath();
             outputPath = new File(outputRootPath, "README.md").getAbsolutePath();
        //生成静态文件
        StaticFileGenerator.copyFilesByHutool(inputPath, outputPath);
       }
}
}
