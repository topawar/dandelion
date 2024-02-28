package com.topawar.maker.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author topawar
 */
@Data
public class TemplateFileMakerConfig {

    private List<FileInfoConfig> fileInfoConfigList;

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
}
