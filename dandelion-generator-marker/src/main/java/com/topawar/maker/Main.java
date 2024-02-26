package com.topawar.maker;


import com.topawar.maker.generator.file.MainGenerator;
import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * @author topawar
 */
public class Main {
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
        MainGenerator mainGenerator = new MainGenerator();
        mainGenerator.doGenerate();
    }
}
