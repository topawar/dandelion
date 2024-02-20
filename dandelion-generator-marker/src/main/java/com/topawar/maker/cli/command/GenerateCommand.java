package com.topawar.maker.cli.command;

import cn.hutool.core.bean.BeanUtil;
import com.topawar.maker.generator.file.MainGenerator;
import com.topawar.maker.model.DataModel;
import lombok.Data;
import picocli.CommandLine;

import java.util.concurrent.Callable;

/**
 * @author topawar
 */
@Data
@CommandLine.Command(name = "generate",description = "生成项目",mixinStandardHelpOptions = true)
public class GenerateCommand implements Callable<Integer> {

    @CommandLine.Option(names = {"-a","--author"},arity = "0..1",description = "作者",interactive = true,echo = true)
    private String author="topawar";

    @CommandLine.Option(names = {"-l","--loop"},arity = "0..1",description = "是否循环",interactive = true,echo = true)
    private boolean loop=false;

    @CommandLine.Option(names = {"-t","--outputText"},arity = "0..1",description = "输出文本",interactive = true,echo = true)
    private String outputText="总数：";

    @Override
    public Integer call() throws Exception {
        DataModel dataModel = new DataModel();
        BeanUtil.copyProperties(this, dataModel);
        MainGenerator.doGenerate(dataModel);
        return 0;
    }
}
