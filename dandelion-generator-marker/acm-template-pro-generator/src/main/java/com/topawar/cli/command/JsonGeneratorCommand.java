package com.topawar.cli.command;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.topawar.generator.MainGenerator;
import com.topawar.model.DataModel;
import lombok.Data;
import picocli.CommandLine;

import java.util.concurrent.Callable;

/**
 * @author topawar
 */
@Data
@CommandLine.Command(name = "json-generate", description = "读取json生成项目", mixinStandardHelpOptions = true)
public class JsonGeneratorCommand implements Callable<Integer> {



    @CommandLine.Option(names = {"-f", "--file"}, arity = "0..1", description = "json文件路径", interactive = true, echo = true)
    private String filePath;

    @Override
    public Integer call() throws Exception {
        String dataJson = FileUtil.readUtf8String(filePath);
        DataModel dataModel = JSONUtil.toBean(dataJson, DataModel.class);
        MainGenerator.doGenerate(dataModel);
        return 0;
    }
}
