package com.topawar.web.model.dto.generator;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.topawar.web.meta.Meta;
import lombok.Data;

/**
 * 编辑请求
 *
 * @author topawar
 */
@Data
public class GeneratorEditRequest implements Serializable {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 基础包
     */
    private String basePackage;

    /**
     * 版本
     */
    private String version;

    /**
     * 作者
     */
    private String author;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 图片
     */
    private String picture;

    /**
     * 文件配置（json字符串）
     */
    private Meta.FileConfig fileConfig;

    /**
     * 模型配置（json字符串）
     */
    private Meta.ModelConfig modelConfig;

    /**
     * 代码生成器产物路径
     */
    private String distPath;



    private static final long serialVersionUID = 1L;
}