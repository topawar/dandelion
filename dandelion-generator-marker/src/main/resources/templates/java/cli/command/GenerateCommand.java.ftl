package ${basePackage}.cli.command;

import cn.hutool.core.bean.BeanUtil;
import ${basePackage}.generator.MainGenerator;
import ${basePackage}.model.DataModel;
import lombok.Data;
import picocli.CommandLine;

import java.util.concurrent.Callable;

/**
 * @author topawar
 */
@Data
@CommandLine.Command(name = "generate",description = "生成项目",mixinStandardHelpOptions = true)
public class GenerateCommand implements Callable<Integer> {

    <#list modelConfig.models as modelInfo>
        <#if modelInfo.description??>
            /**
            * ${modelInfo.description}
            */
        </#if>
        @CommandLine.Option(names = {<#if modelInfo.abbr??>"-${modelInfo.abbr}</#if>","--${modelInfo.fieldName}"},arity = "0..1",description = "${modelInfo.description}",interactive = true,echo = true)
        private ${modelInfo.type} ${modelInfo.fieldName} <#if modelInfo.defaultValue??>=${modelInfo.defaultValue?c} </#if>;
    </#list>

    @Override
    public Integer call() throws Exception {
        DataModel dataModel = new DataModel();
        BeanUtil.copyProperties(this, dataModel);
        MainGenerator.doGenerate(dataModel);
        return 0;
    }
}
