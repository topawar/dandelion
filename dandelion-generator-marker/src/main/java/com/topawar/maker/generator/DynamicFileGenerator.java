package com.topawar.maker.generator;

import cn.hutool.core.io.FileUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author topawar
 */
public class DynamicFileGenerator {

    /**
     *
     * @param inputPath
     * @param outputPath
     * @param model
     * @throws IOException
     * @throws TemplateException
     */
    public static void doGenerate(String inputPath,String outputPath, Object model) throws IOException, TemplateException {
        //new freemarkerConfig对象
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
        //指定模板文件的路径
        File templateDir = new File(inputPath).getParentFile();
        configuration.setDirectoryForTemplateLoading(templateDir);
        //设置字符集，数字格式化
        configuration.setDefaultEncoding("UTF-8");
        configuration.setNumberFormat("0.######");
        //创建模板对象
        Template template = configuration.getTemplate(new File(inputPath).getName());
        //若文件不存在创建文件
        if (!FileUtil.exist(outputPath)){
            FileUtil.touch(outputPath);
        }
        //生成文件
        FileWriter out = new FileWriter(outputPath);
        template.process(model, out);
        //关闭流
        out.close();
    }
}
