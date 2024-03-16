package com.topawar.web.model.dto.generator;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author topawar
 */
@Data
public class GeneratorUseRequest {
    /**
     * 生成器id
     */
    private Long id;

    /**
     * 数据模型
     */
    private Map<String,Object> dataModel;


}
