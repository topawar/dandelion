package ${basePackage}.model;

import lombok.Data;

<#macro generateModel modelInfo>
    <#if modelInfo.description??>
        /**
        * ${modelInfo.description}
        */
    </#if>
    public ${modelInfo.type} ${modelInfo.fieldName} <#if modelInfo.defaultValue??>=${modelInfo.defaultValue?c} </#if>;
</#macro>

/**
 * 数据模型
 */
@Data
public class DataModel {

    <#list modelConfig.models as modelInfo>
    <#if modelInfo.groupKey??>
        <#if modelInfo.groupName??>
            /**
            * ${modelInfo.groupName}
            */
        </#if>
        public ${modelInfo.type} ${modelInfo.groupKey} = new ${modelInfo.type}();
        /**
        * ${modelInfo.description}
        */
        @Data
        public static class ${modelInfo.type} {
            <#list modelInfo.models as model>
                <@generateModel modelInfo=model></@generateModel>
            </#list>
        }
    <#else>
    <@generateModel modelInfo=modelInfo></@generateModel>
    </#if>
    </#list>
}
