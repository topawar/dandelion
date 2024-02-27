package ${basePackage}.cli.command;

import cn.hutool.core.bean.BeanUtil;
import ${basePackage}.generator.MainGenerator;
import ${basePackage}.model.DataModel;
import lombok.Data;
import picocli.CommandLine;

import java.util.concurrent.Callable;
<#macro generateOption modelInfo>
    <#if modelInfo.description??>
        /**
        * ${modelInfo.description}
        */
    </#if>
    @CommandLine.Option(names = {<#if modelInfo.abbr??>"-${modelInfo.abbr}</#if>","--${modelInfo.fieldName}"},arity = "0..1",description = "${modelInfo.description}",interactive = true,echo = true)
    private ${modelInfo.type} ${modelInfo.fieldName} <#if modelInfo.defaultValue??>=${modelInfo.defaultValue?c} </#if>;
</#macro>

<#macro generateCommand modelInfo>
    CommandLine commandLine=new CommandLine(${modelInfo.type}Command.class);
    commandLine.execute(${modelInfo.allArgs});
</#macro>
/**
 * @author topawar
 */
@Data
@CommandLine.Command(name = "generate",description = "生成项目",mixinStandardHelpOptions = true)
public class GenerateCommand implements Callable<Integer> {

    <#list modelConfig.models as modelInfo>

    <#if modelInfo.groupKey??>
        <#if modelInfo.groupName??>
            /**
            * ${modelInfo.groupName}
            */
        </#if>
        static DataModel.${modelInfo.type} ${modelInfo.groupKey} = new DataModel.${modelInfo.type}();
        @CommandLine.Command(name = "${modelInfo.groupKey}")
        @Data
        public static class ${modelInfo.type}Command implements Runnable{

        <#list modelInfo.models as model>
            <@generateOption modelInfo=model></@generateOption>
        </#list>

            @Override
            public void run(){
        <#list modelInfo.models as subModelInfo>
            ${modelInfo.groupKey}.${subModelInfo.fieldName} = ${subModelInfo.fieldName};
        </#list>
            }
        }
    <#else>
    <@generateOption modelInfo=modelInfo></@generateOption>
    </#if>
    </#list>

    @Override
    public Integer call() throws Exception {
        <#list modelConfig.models as modelInfo>
            <#if modelInfo.groupKey??>
                <#if modelInfo.condition??>
                    if(${modelInfo.condition}){
                    <@generateCommand modelInfo=modelInfo></@generateCommand>
                    }
                    <#else>
                    <@generateCommand modelInfo=modelInfo></@generateCommand>
                </#if>
            </#if>
        </#list>
        DataModel dataModel = new DataModel();
        BeanUtil.copyProperties(this, dataModel);
        <#list modelConfig.models as modelInfo>
            <#if modelInfo.groupKey??>
                dataModel.${modelInfo.groupKey} = ${modelInfo.groupKey};
            </#if>
        </#list>
        MainGenerator.doGenerate(dataModel);
        return 0;
    }
}
