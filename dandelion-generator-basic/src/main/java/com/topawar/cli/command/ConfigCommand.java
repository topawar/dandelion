package com.topawar.cli.command;

import cn.hutool.core.util.ReflectUtil;
import com.topawar.model.MainTemplateConfig;
import picocli.CommandLine;

import java.lang.reflect.Field;

/**
 * @author topawar
 */
@CommandLine.Command(name = "config",description = "查看参数信息",mixinStandardHelpOptions = true)
public class ConfigCommand implements Runnable{

    @Override
    public void run() {
        Field[] fields = ReflectUtil.getFields(MainTemplateConfig.class);
        for (Field field : fields) {
            System.out.println("参数名称："+field.getName());
            System.out.println("参数类型："+field.getType());
        }
    }
}
