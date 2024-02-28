package com.topawar.maker.model;

import lombok.Data;

/**
 * @author topawar
 */
@Data
public class FileFilterConfig {
    /**
     * 过滤范围
     */
    private String range;

    /**
     * 过滤规则
     */
    private String rule;

    /**
     * 过滤值
     */
    private String value;
}
