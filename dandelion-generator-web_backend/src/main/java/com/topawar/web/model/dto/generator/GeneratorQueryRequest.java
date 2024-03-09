package com.topawar.web.model.dto.generator;

import com.topawar.web.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询请求
 *
 * @author topawar
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GeneratorQueryRequest extends PageRequest implements Serializable {

    private Long notId;

    private Long id;

    private String searchText;

    /**
     * 产物包路径
     */
    private String distPath;

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
     * 状态
     */
    private Integer status;

    /**
     * 创建用户 id
     */
    private Long userId;


    private static final long serialVersionUID = 1L;
}