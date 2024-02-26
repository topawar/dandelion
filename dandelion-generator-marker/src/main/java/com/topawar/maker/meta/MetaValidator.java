package com.topawar.maker.meta;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.topawar.maker.enums.FileGenerateTypeEnum;
import com.topawar.maker.enums.FileTypeEnum;
import com.topawar.maker.enums.ModelTypeEnum;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

/**
 * 元数据信息校验
 *
 * @author topawar
 */
public class MetaValidator {
    public static void doValidAndFill(Meta meta) {
        validAndFillMetaRoot(meta);
        //fileConfig默认值校验
        validAndFillFileConfig(meta);
        //modelConfig 默认值校验
        validAndFillModelConfig(meta);
    }

    private static void validAndFillModelConfig(Meta meta) {
        Meta.ModelConfig modelConfig = meta.getModelConfig();
        if (null == modelConfig) {
            return;
        }
        List<Meta.ModelConfig.ModelInfo> models = modelConfig.getModels();
        if (!CollectionUtil.isNotEmpty(models)) {
            return;
        }
        for (Meta.ModelConfig.ModelInfo model : models) {
            String fieldName = model.getFieldName();
            if (StrUtil.isBlank(fieldName)) {
                throw new MetaException("字段名不能为空");
            }
            String type = model.getType();
            if (StrUtil.isBlank(type)) {
                model.setType(ModelTypeEnum.STRING.getValue());
            }
        }
    }

    private static void validAndFillFileConfig(Meta meta) {
        Meta.FileConfig fileConfig = meta.getFileConfig();
        if (fileConfig == null) {
            return;
        }
        //必填项校验
        String sourceRootPath = fileConfig.getSourceRootPath();
        if (StrUtil.isEmpty(sourceRootPath)) {
            throw new MetaException("sourceRootPath 不能为空");
        }
        //默认为.source + sourceRootPath 最后一个层级
        String inputRootPath = fileConfig.getInputRootPath();
        String defaultInputRootPath = ".source" + File.separator + FileUtil.getLastPathEle(Paths.get(sourceRootPath)).getFileSystem().toString();
        if (StrUtil.isEmpty(inputRootPath)) {
            fileConfig.setInputRootPath(defaultInputRootPath);
        }

        // output路径默认为当前路径下的generate
        String outputRootPath = fileConfig.getOutputRootPath();
        String defaultOutputPath = "generate";
        if (StrUtil.isEmpty(outputRootPath)) {
            fileConfig.setOutputRootPath(defaultOutputPath);
        }

        String type = fileConfig.getType();
        String defaultType = FileTypeEnum.DIR.getText();
        if (StrUtil.isEmpty(type)) {
            fileConfig.setType(defaultType);
        }

        // fileInfo默认值
        List<Meta.FileConfig.FileInfo> fileInfos = fileConfig.getFiles();
        if (!CollectionUtil.isNotEmpty(fileInfos)) {
            return;
        }
        for (Meta.FileConfig.FileInfo fileInfo : fileInfos) {
            //inputpath 校验
            String inputPath = fileInfo.getInputPath();
            if (StrUtil.isBlank(inputPath)) {
                throw new MetaException("inputPath 不能为空");
            }
            //outputPath默认为inputpath-ftl
            String outputPath = fileInfo.getOutputPath();
            if (StrUtil.isEmpty(outputPath)) {
                String subInputPath = inputPath.substring(0, inputPath.length() - 4);
                fileInfo.setOutputPath(subInputPath);
            }
            // 有后缀为文件，无后缀为文件夹
            String fileInfoType = fileInfo.getType();
            if (StrUtil.isBlank(fileInfoType)) {
                if (StrUtil.isBlank(FileUtil.getSuffix(inputPath))) {
                    fileInfo.setType(FileTypeEnum.FILE.getValue());
                } else {
                    fileInfo.setType(FileTypeEnum.DIR.getValue());
                }
            }

            // 后缀为.ftl为动态
            String fileInfoGenerateType = fileInfo.getGenerateType();
            if (StrUtil.isBlank(fileInfoGenerateType)) {
                if (inputPath.endsWith(".ftl")) {
                    fileInfo.setType(FileGenerateTypeEnum.DYNAMIC.getValue());
                } else {
                    fileInfo.setType(FileGenerateTypeEnum.STATIC.getValue());
                }
            }
        }
    }

    private static void validAndFillMetaRoot(Meta meta) {
        //基础信息及默认值校验
        String name = StrUtil.blankToDefault(meta.getName(), "my-generator");
        String description = StrUtil.emptyToDefault(meta.getDescription(), "代码生成器");
        String author = StrUtil.emptyToDefault(meta.getAuthor(), "topawar");
        String basePackage = StrUtil.blankToDefault(meta.getBasePackage(), "com.topawar");
        String version = StrUtil.emptyToDefault(meta.getVersion(), "1.0");
        String createtime = StrUtil.emptyToDefault(meta.getCreateTime(), DateUtil.now());
        meta.setName(name);
        meta.setDescription(description);
        meta.setAuthor(author);
        meta.setBasePackage(basePackage);
        meta.setVersion(version);
        meta.setCreateTime(createtime);
    }

}
