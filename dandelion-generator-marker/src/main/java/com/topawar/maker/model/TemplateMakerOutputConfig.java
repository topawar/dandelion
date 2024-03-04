package com.topawar.maker.model;

import lombok.Data;

/**
 * @author topawar
 */
@Data
public class TemplateMakerOutputConfig {
    //移除未分组文件中的同名文件
    private boolean removeGroupFilesFormRoot = true;
}
