package com.topawar.maker;


import com.topawar.maker.generator.file.GenerateTemplate;
import com.topawar.maker.generator.file.MainGenerator;
import com.topawar.maker.generator.file.ZipGenerator;
import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * @author topawar
 */
public class Main {
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
//        GenerateTemplate mainGenerator = new MainGenerator();
        GenerateTemplate zipGenerator = new ZipGenerator();
        zipGenerator.doGenerate();
    }
}
