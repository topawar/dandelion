package com.topawar;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreeMakerTest {

    @Test
    public void test() throws IOException, TemplateException {
        //FreeMarker的版本号
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
        configuration.setDirectoryForTemplateLoading(new File("src/main/resources/templates"));
        configuration.setDefaultEncoding("UTF-8");
        configuration.setNumberFormat("0.######");
        Template template = configuration.getTemplate("index.htm.ftl");
        /**
         * {
         *   "currentYear": 2023,
         *   "menuItems": [
         *     {
         *       "url": "https://codefather.cn",
         *       "label": "编程导航",
         *     },
         *     {
         *       "url": "https://laoyujianli.com",
         *       "label": "老鱼简历",
         *     }
         *   ]
         * }
         */
        List<Map<String, Object>> menuList = new ArrayList<>();
        HashMap<String, Object> dataModel = new HashMap<>();
        HashMap<String, Object> menuItem2 = new HashMap<>();
        HashMap<String, Object> menuItem3 = new HashMap<>();
        dataModel.put("currentYear", 2023);
        menuItem2.put("url", "https://codefather.cn");
        menuItem2.put("label", "编程导航");
        menuItem3.put("url", "老鱼简历");
        menuItem3.put("label", "https://laoyujianli.com");
        menuList.add(menuItem2);
        menuList.add(menuItem3);
        dataModel.put("menuItems", menuList);
        FileWriter out = new FileWriter("myweb.html");
        template.process(dataModel, out);
        out.close();
    }
}
