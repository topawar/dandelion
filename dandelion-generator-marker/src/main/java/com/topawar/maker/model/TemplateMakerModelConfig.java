package com.topawar.maker.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author topawar
 */
@Data
public class TemplateMakerModelConfig {

    private List<ModelInfoConfig> modelInfoConfigs;

    /**
     * 模型分组
     */
    private ModelGroupConfig modelGroupConfig;

    @Data
    @NoArgsConstructor
    public static class ModelInfoConfig{
        /**
         * 字段名
         */
        private String fieldName;

        /**
         * 字段类型
         */
        private String type;

        /**
         * 描述
         */
        private String description;

        /**
         * 命令参数
         */
        private String abbr;

        /**
         * 默认值
         */
        private Object defaultValue;

        /**
         * 替换文本
         */
        private String replaceText;
    }

    @Data
    @NoArgsConstructor
    public static class ModelGroupConfig {
        /**
         * 分组的key
         */
        private String groupKey;
        /**
         * 名称
         */
        private String groupName;

        /**
         * 条件
         */
        private String condition;
    }
}
