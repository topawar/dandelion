package com.topawar.cli.command;

import cn.hutool.core.bean.BeanUtil;
import com.topawar.generator.MainGenerator;
import com.topawar.model.DataModel;
import lombok.Data;
import picocli.CommandLine;

import java.util.concurrent.Callable;
/**
 * @author topawar
 */
@Data
@CommandLine.Command(name = "generate",description = "生成项目",mixinStandardHelpOptions = true)
public class GenerateCommand implements Callable<Integer> {


        /**
        * 是否生成循环
        */
    @CommandLine.Option(names = {"-l","--loop"},arity = "0..1",description = "是否生成循环",interactive = true,echo = true)
    private boolean loop =false ;

        /**
        * 是否生成git文件
        */
    @CommandLine.Option(names = {"-n","--needGit"},arity = "0..1",description = "是否生成git文件",interactive = true,echo = true)
    private boolean needGit =true ;

            /**
            * 核心模板
            */
        static DataModel.MainTemplate mainTemplate = new DataModel.MainTemplate();
        @CommandLine.Command(name = "mainTemplate")
        @Data
        public static class MainTemplateCommand implements Runnable{

        /**
        * 作者注释
        */
    @CommandLine.Option(names = {"-a","--author"},arity = "0..1",description = "作者注释",interactive = true,echo = true)
    private String author ="yupi" ;
        /**
        * 输出信息
        */
    @CommandLine.Option(names = {"-o","--outputText"},arity = "0..1",description = "输出信息",interactive = true,echo = true)
    private String outputText ="sum = " ;

            @Override
            public void run(){
            mainTemplate.author = author;
            mainTemplate.outputText = outputText;
            }
        }

    @Override
    public Integer call() throws Exception {
                    if(loop){
    CommandLine mainTemplateCommandLine=new CommandLine(MainTemplateCommand.class);
    mainTemplateCommandLine.execute("--author", "--outputText");
                    }
        DataModel dataModel = new DataModel();
        BeanUtil.copyProperties(this, dataModel);
                dataModel.mainTemplate = mainTemplate;
        MainGenerator.doGenerate(dataModel);
        return 0;
    }
}
