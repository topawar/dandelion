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
            * MySQL数据库配置
            */
        static DataModel.MysqlConfig mysqlConfig = new DataModel.MysqlConfig();
        @CommandLine.Command(name = "mysqlConfig")
        @Data
        public static class MysqlConfigCommand implements Runnable{

        /**
        * 密码
        */
    @CommandLine.Option(names = {"--password"},arity = "0..1",description = "密码",interactive = true,echo = true)
    private String password ="123456" ;
        /**
        * 地址
        */
    @CommandLine.Option(names = {"--url"},arity = "0..1",description = "地址",interactive = true,echo = true)
    private String url ="jdbc:mysql://localhost:3306/my_db" ;
        /**
        * 用户名
        */
    @CommandLine.Option(names = {"--username"},arity = "0..1",description = "用户名",interactive = true,echo = true)
    private String username ="root" ;

            @Override
            public void run(){
            mysqlConfig.password = password;
            mysqlConfig.url = url;
            mysqlConfig.username = username;
            }
        }

            /**
            * 接口文档配置
            */
        static DataModel.DocsConfig docsConfig = new DataModel.DocsConfig();
        @CommandLine.Command(name = "docsConfig")
        @Data
        public static class DocsConfigCommand implements Runnable{

        /**
        * 接口文档描述
        */
    @CommandLine.Option(names = {"--description"},arity = "0..1",description = "接口文档描述",interactive = true,echo = true)
    private String description ="springboot-init" ;
        /**
        * 接口文档标题
        */
    @CommandLine.Option(names = {"--title"},arity = "0..1",description = "接口文档标题",interactive = true,echo = true)
    private String title ="接口文档" ;
        /**
        * 接口文档版本
        */
    @CommandLine.Option(names = {"--version"},arity = "0..1",description = "接口文档版本",interactive = true,echo = true)
    private String version ="1.0" ;

            @Override
            public void run(){
            docsConfig.description = description;
            docsConfig.title = title;
            docsConfig.version = version;
            }
        }

        /**
        * 是否开启接口文档功能
        */
    @CommandLine.Option(names = {"--needDocs"},arity = "0..1",description = "是否开启接口文档功能",interactive = true,echo = true)
    private boolean needDocs =true ;

        /**
        * 是否开启帖子功能
        */
    @CommandLine.Option(names = {"--needPost"},arity = "0..1",description = "是否开启帖子功能",interactive = true,echo = true)
    private boolean needPost =true ;

        /**
        * 是否开启跨域功能
        */
    @CommandLine.Option(names = {"--needCors"},arity = "0..1",description = "是否开启跨域功能",interactive = true,echo = true)
    private boolean needCors =true ;

        /**
        * 是否开启ES功能
        */
    @CommandLine.Option(names = {"--needEs"},arity = "0..1",description = "是否开启ES功能",interactive = true,echo = true)
    private boolean needEs =true ;

        /**
        * 基础包名
        */
    @CommandLine.Option(names = {"--basePackage"},arity = "0..1",description = "基础包名",interactive = true,echo = true)
    private String basePackage ="com.yupi" ;

        /**
        * 是否开启Redis功能
        */
    @CommandLine.Option(names = {"--needRedis"},arity = "0..1",description = "是否开启Redis功能",interactive = true,echo = true)
    private boolean needRedis =true ;

    @Override
    public Integer call() throws Exception {
    CommandLine mysqlConfigCommandLine=new CommandLine(MysqlConfigCommand.class);
    mysqlConfigCommandLine.execute("--password", "--url", "--username");
                    if(needDocs){
    CommandLine docsConfigCommandLine=new CommandLine(DocsConfigCommand.class);
    docsConfigCommandLine.execute("--description", "--title", "--version");
                    }
        DataModel dataModel = new DataModel();
        BeanUtil.copyProperties(this, dataModel);
                dataModel.mysqlConfig = mysqlConfig;
                dataModel.docsConfig = docsConfig;
        MainGenerator.doGenerate(dataModel);
        return 0;
    }
}
