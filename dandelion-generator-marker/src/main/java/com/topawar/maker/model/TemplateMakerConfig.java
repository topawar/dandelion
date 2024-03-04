package com.topawar.maker.model;

import com.topawar.maker.meta.Meta;
import lombok.Data;

/**
 * @author topawar
 */
@Data
public class TemplateMakerConfig {
    private Long id;
    private Meta meta = new Meta();
    private String originProjectPath;
    private TemplateMakerFileConfig fileConfig = new TemplateMakerFileConfig();
    private TemplateMakerModelConfig modelConfig = new TemplateMakerModelConfig();
    private TemplateMakerOutputConfig outputConfig = new TemplateMakerOutputConfig();
}
