package com.topawar.model;

import lombok.Data;

@Data
public class MainTemplateConfig {
    private String author="topawar";
    private boolean loop=false;
    private String outputText="输出的结果为：";
}
