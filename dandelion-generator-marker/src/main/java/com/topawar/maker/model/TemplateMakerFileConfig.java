package com.topawar.maker.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author topawar
 */
@Data
public class TemplateMakerFileConfig {

    private List<FileInfoConfig> fileInfoConfigList;

    /**
     * 文件分组配置
     */
    private FileGroupConfig fileGroupConfig;



    @Data
    @NoArgsConstructor
    public static class FileInfoConfig{
        /**
         * 路径
         */
        private String path;

        /**
         * 过滤规则列表
         */
        private List<FileFilterConfig> fileFilterConfigList;
    }

    /**
     * 文件分组，对应meta.json
     */
    @Data
    @NoArgsConstructor
    public static class FileGroupConfig {
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
