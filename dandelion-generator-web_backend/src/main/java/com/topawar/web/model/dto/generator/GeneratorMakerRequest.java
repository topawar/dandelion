package com.topawar.web.model.dto.generator;

import com.topawar.maker.meta.Meta;
import lombok.Data;

/**
 * @author topawar
 */
@Data
public class GeneratorMakerRequest {
    /**
     * 压缩包路径
     */
    private String zipPath;

    private Meta meta;
}
