package com.topawar.web.model.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.topawar.maker.meta.Meta;
import com.topawar.web.model.entity.Generator;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 代码生成器
 *
 * @TableName generator
 */
@Data
public class GeneratorVO implements Serializable {
    /**
     * id
     */
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

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private UserVO userVO;

    private static final long serialVersionUID = 1L;

    /**
     * 实体转vo
     * @param generatorVo
     * @return
     */
    public static Generator voToObj(GeneratorVO generatorVo) {
        if (generatorVo == null) {
            return null;
        }
        Generator generator = new Generator();
        BeanUtil.copyProperties(generatorVo, generator);
        Meta.FileConfig fileConfig = generatorVo.getFileConfig();
        Meta.ModelConfig modelConfig = generatorVo.getModelConfig();
        List<String> tagList = generatorVo.getTags();
        generator.setFileConfig(JSONUtil.toJsonStr(fileConfig));
        generator.setModelConfig(JSONUtil.toJsonStr(modelConfig));
        generator.setTags(JSONUtil.toJsonStr(tagList));
        return generator;
    }

    /**
     * vo转对象
     * @param generator
     * @return
     */
//    public static GeneratorVO objToVo(Generator generator) {
//        if (generator == null) {
//            return null;
//        }
//        GeneratorVO generatorVo = new GeneratorVO();
//        BeanUtil.copyProperties(generator, generatorVo);
//        String fileConfig = generator.getFileConfig();
//        String modelConfig = generator.getModelConfig();
//        String tagList = generator.getTags();
//        generatorVo.setFileConfig(JSONUtil.toBean(fileConfig, Meta.FileConfig.class));
//        generatorVo.setModelConfig(JSONUtil.toBean(modelConfig, Meta.ModelConfig.class));
//        generatorVo.setTags(JSONUtil.toList(tagList, String.class));
//        return generatorVo;
//    }

    /**
     * 对象转包装类
     *
     * @param generator
     * @return
     */
    public static GeneratorVO objToVo(Generator generator) {
        if (generator == null) {
            return null;
        }
        GeneratorVO generatorVO = new GeneratorVO();
        BeanUtils.copyProperties(generator, generatorVO);
        generatorVO.setTags(JSONUtil.toList(generator.getTags(), String.class));
        generatorVO.setFileConfig(JSONUtil.toBean(generator.getFileConfig(), Meta.FileConfig.class));
        generatorVO.setModelConfig(JSONUtil.toBean(generator.getModelConfig(), Meta.ModelConfig.class));
        return generatorVO;
    }
}