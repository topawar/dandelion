package com.topawar.maker.template;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.topawar.maker.enums.FileGenerateTypeEnum;
import com.topawar.maker.enums.FileTypeEnum;
import com.topawar.maker.meta.Meta;
import com.topawar.maker.model.TemplateMakerConfig;
import com.topawar.maker.model.TemplateMakerFileConfig;
import com.topawar.maker.model.TemplateMakerModelConfig;
import com.topawar.maker.model.TemplateMakerOutputConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author topawar
 */
public class TemplateMaker {

    /**
     * 文件去重
     *
     * @param fileInfoList
     * @return
     */
    private static List<Meta.FileConfig.FileInfo> distinctFiles(List<Meta.FileConfig.FileInfo> fileInfoList) {
        //通过groupkey分组
        Map<String, List<Meta.FileConfig.FileInfo>> groupFileInfoListMap = fileInfoList.stream()
                .filter(fileInfo -> StrUtil.isNotBlank(fileInfo.getGroupKey()))
                .collect(Collectors.groupingBy(Meta.FileConfig.FileInfo::getGroupKey));

        //同组类的文件配置合并
        Map<String, Meta.FileConfig.FileInfo> mergeGroupFileInfoListMap = new HashMap<>();
        for (Map.Entry<String, List<Meta.FileConfig.FileInfo>> entry : groupFileInfoListMap.entrySet()) {
            List<Meta.FileConfig.FileInfo> tempFileInfoList = entry.getValue();
            List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>(tempFileInfoList.stream()
                    .flatMap(fileInfo -> fileInfo.getFiles()
                            .stream()).collect(
                            Collectors.toMap(Meta.FileConfig.FileInfo::getOutputPath,
                                    o -> o, (e, r) -> r)
                    ).values());
            //获取集合的最后一个元素
            Meta.FileConfig.FileInfo newFileInfo = CollUtil.getLast(tempFileInfoList);
            //groupkey为空的值加入集合
            newFileInfo.setFiles(newFileInfoList);
            String groupKey = entry.getKey();
            mergeGroupFileInfoListMap.put(groupKey, newFileInfo);
        }
        List<Meta.FileConfig.FileInfo> resultList = new ArrayList<>(mergeGroupFileInfoListMap.values());
        //获取未分组的文件列表
        List<Meta.FileConfig.FileInfo> noGroupFileList = fileInfoList.stream().filter(fileInfo -> StrUtil.isBlank(fileInfo.getGroupKey())).collect(Collectors.toList());

        resultList.addAll(new ArrayList<>(noGroupFileList
                .stream()
                .collect(
                        Collectors.toMap(Meta.FileConfig.FileInfo::getOutputPath, o -> o, (e, r) -> r)
                ).values()));
        return resultList;
    }

    private static List<Meta.ModelConfig.ModelInfo> distinctModels(List<Meta.ModelConfig.ModelInfo> modelInfoList) {
        Map<String, List<Meta.ModelConfig.ModelInfo>> modelGroupKeyMap = modelInfoList.stream().filter(modelInfo -> StrUtil.isNotBlank(modelInfo.getGroupKey()))
                .collect(Collectors
                        .groupingBy(Meta.ModelConfig.ModelInfo::getGroupKey));
        Map<String, Meta.ModelConfig.ModelInfo> MergemodelGroupMap = new HashMap<>();
        for (Map.Entry<String, List<Meta.ModelConfig.ModelInfo>> entry : modelGroupKeyMap.entrySet()) {
            List<Meta.ModelConfig.ModelInfo> tempModelInfoList = entry.getValue();
            List<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>(tempModelInfoList.stream().flatMap(
                            modelInfo -> modelInfo.getModels()
                                    .stream()).collect(Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r))
                    .values());
            Meta.ModelConfig.ModelInfo lastModelInfo = CollUtil.getLast(tempModelInfoList);
            lastModelInfo.setModels(newModelInfoList);
            String groupKey = entry.getKey();
            MergemodelGroupMap.put(groupKey, lastModelInfo);
        }
        List<Meta.ModelConfig.ModelInfo> resultList = new ArrayList<>(MergemodelGroupMap.values());
        //获取未分组的model信息
        List<Meta.ModelConfig.ModelInfo> noGruopModelInfoList = modelInfoList.stream().filter(modelInfo -> StrUtil.isBlank(modelInfo.getGroupKey())).collect(Collectors.toList());
        resultList.addAll(new ArrayList<>(noGruopModelInfoList
                .stream()
                .collect(
                        Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r)
                ).values()));
        return resultList;

    }


    /**
     * @param newMeta
     * @param originProjectPath
     * @param templateFileConfig
     * @param templateMakerModelConfig
     * @param id
     * @return
     */
    private static long makeTemplate(Meta newMeta, String originProjectPath,
                                     TemplateMakerFileConfig templateFileConfig,
                                     TemplateMakerModelConfig templateMakerModelConfig,
                                     TemplateMakerOutputConfig outputConfig,
                                     Long id) {
        if (id == null) {
            id = IdUtil.getSnowflakeNextId();
        }
        //输入文件信息
        String projectPath = System.getProperty("user.dir");
        //复制目录
        String tempDir = projectPath + File.separator + ".temp";
        String templatePath = tempDir + File.separator + id;
        if (!FileUtil.exist(templatePath)) {
            FileUtil.mkdir(templatePath);
            FileUtil.copy(originProjectPath, templatePath, false);
        }
        //非第一次生成直接获取第一个层级路径
        String sourceRootPath = FileUtil.loopFiles(new File(templatePath), 1, null)
                .stream()
                .filter(File::isDirectory)
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getAbsolutePath();
        // 注意 win 系统需要对路径进行转义
        sourceRootPath = sourceRootPath.replaceAll("\\\\", "/");

        //文件模板生成，如果是文件夹向下遍历
        List<Meta.FileConfig.FileInfo> fileInfoList = makeFileTemplates(templateFileConfig, templateMakerModelConfig, sourceRootPath);
        //模型列表
        List<Meta.ModelConfig.ModelInfo> newModelInfoList = makeModelTemplates(templateMakerModelConfig);

        //生成meta.json文件
        String fileMetaOutputPath = templatePath + File.separator + "meta.json";
        if (FileUtil.exist(fileMetaOutputPath)) {
            Meta oldMeta = JSONUtil.toBean(FileUtil.readUtf8String(fileMetaOutputPath), Meta.class);
            BeanUtil.copyProperties(oldMeta, newMeta, CopyOptions.create().ignoreNullValue());
            newMeta = oldMeta;
            //去重
            List<Meta.FileConfig.FileInfo> fileInfos = newMeta.getFileConfig().getFiles();
            fileInfos.addAll(fileInfoList);
            List<Meta.FileConfig.FileInfo> distinctFiles = distinctFiles(fileInfos);
            List<Meta.ModelConfig.ModelInfo> modelInfos = newMeta.getModelConfig().getModels();
            modelInfos.addAll(newModelInfoList);
            List<Meta.ModelConfig.ModelInfo> distinctModels = distinctModels(modelInfos);
            //追加
            newMeta.getFileConfig().setFiles(distinctFiles);
            newMeta.getModelConfig().setModels(distinctModels);
        } else {
            //注入文件配置信息
            Meta.FileConfig fileConfig = new Meta.FileConfig();
            fileConfig.setSourceRootPath(sourceRootPath);
            ArrayList<Meta.FileConfig.FileInfo> fileInfos = new ArrayList<>();
            fileInfos.addAll(fileInfoList);
            fileConfig.setFiles(fileInfos);

            //注入模型信息
            Meta.ModelConfig modelConfig = new Meta.ModelConfig();
            ArrayList<Meta.ModelConfig.ModelInfo> modelInfos = new ArrayList<>();
            modelInfos.addAll(newModelInfoList);
            modelConfig.setModels(modelInfos);

            newMeta.setFileConfig(fileConfig);
            newMeta.setModelConfig(modelConfig);

        }
        //输出文件配置不为空
        if (outputConfig != null) {
            if (outputConfig.isRemoveGroupFilesFormRoot()) {
                newMeta.getFileConfig().setFiles(TemplateMakerUtils
                        .removeGroupFilesFromRoot(newMeta.getFileConfig().getFiles()));
            }
        }
        //生成元数据信息文件
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(newMeta), fileMetaOutputPath);
        return id;
    }

    private static List<Meta.ModelConfig.ModelInfo> makeModelTemplates(TemplateMakerModelConfig templateMakerModelConfig) {
        List<Meta.ModelConfig.ModelInfo> emptyModelInfoList = new ArrayList<>();
        if (templateMakerModelConfig == null) {
            return emptyModelInfoList;
        }

        if (CollUtil.isEmpty(templateMakerModelConfig.getModels())) {
            return emptyModelInfoList;
        }

        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigList = templateMakerModelConfig.getModels();
        List<Meta.ModelConfig.ModelInfo> modelInfoList = modelInfoConfigList.stream().map(modelInfoConfig -> {
            Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
            BeanUtil.copyProperties(modelInfoConfig, modelInfo);
            return modelInfo;
        }).collect(Collectors.toList());
        List<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>();
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        if (modelGroupConfig != null) {
            Meta.ModelConfig.ModelInfo groupModelInfo = new Meta.ModelConfig.ModelInfo();
            BeanUtil.copyProperties(modelGroupConfig,groupModelInfo);
            groupModelInfo.setModels(modelInfoList);
            newModelInfoList.add(groupModelInfo);
        } else {
            newModelInfoList.addAll(modelInfoList);
        }
        return newModelInfoList;
    }

    private static List<Meta.FileConfig.FileInfo> makeFileTemplates(TemplateMakerFileConfig templateFileConfig,
                                                                    TemplateMakerModelConfig templateMakerModelConfig,
                                                                    String sourceRootPath) {
        List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();

        if (templateFileConfig == null) {
            return fileInfoList;
        }

        if (CollUtil.isEmpty(templateFileConfig.getFiles())) {
            return fileInfoList;
        }
        List<TemplateMakerFileConfig.FileInfoConfig> fileInfoConfigList = templateFileConfig.getFiles();
        for (TemplateMakerFileConfig.FileInfoConfig fileInfoConfig : fileInfoConfigList) {

            String inputfilePath = fileInfoConfig.getPath();
            //如果是相对路径要转换为绝对路局
            if (!inputfilePath.startsWith(sourceRootPath)) {
                inputfilePath = sourceRootPath + File.separator + inputfilePath;
            }

            //获取过滤后的文件列表
            List<File> fileList = FileFilter.doFileFilter(inputfilePath, fileInfoConfig.getFilterConfigList());
            fileList = fileList.stream().filter(file -> !file.getAbsolutePath().endsWith(".ftl")).collect(Collectors.toList());
            for (File file : fileList) {
                Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(templateMakerModelConfig,
                        sourceRootPath, file,fileInfoConfig);
                fileInfoList.add(fileInfo);
            }
        }

        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = templateFileConfig.getFileGroupConfig();
        if (fileGroupConfig != null) {
            String groupKey = fileGroupConfig.getGroupKey();
            String groupName = fileGroupConfig.getGroupName();
            String condition = fileGroupConfig.getCondition();
            //新增分组配置
            Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
            fileInfo.setGroupKey(groupKey);
            fileInfo.setGroupName(groupName);
            fileInfo.setCondition(condition);
            fileInfo.setType(FileTypeEnum.GROUP.getValue());
            fileInfo.setFiles(fileInfoList);
            //先清空原文件列表，在放到分组中
            fileInfoList = new ArrayList<>();
            fileInfoList.add(fileInfo);
        }
        return fileInfoList;
    }

    /**
     * @param templateMakerConfig
     * @return
     */
    public static long makeTemplate(TemplateMakerConfig templateMakerConfig) {
        Meta meta = templateMakerConfig.getMeta();
        String originProjectPath = templateMakerConfig.getOriginProjectPath();
        TemplateMakerFileConfig templateMakerFileConfig = templateMakerConfig.getFileConfig();
        TemplateMakerModelConfig templateMakerModelConfig = templateMakerConfig.getModelConfig();
        TemplateMakerOutputConfig outputConfig = templateMakerConfig.getOutputConfig();
        Long id = templateMakerConfig.getId();
        return makeTemplate(meta, originProjectPath, templateMakerFileConfig, templateMakerModelConfig, outputConfig, id);

    }

    /**
     * 制作文件模板
     *
     * @param sourceRootPath
     * @param inputFile
     * @return
     */
    private static Meta.FileConfig.FileInfo makeFileTemplate(TemplateMakerModelConfig templateMakerModelConfig,
                                                             String sourceRootPath, File inputFile
            , TemplateMakerFileConfig.FileInfoConfig fileInfoConfig) {

        // 注意 win 系统需要对路径进行转义
        String fileInputAbsolutePath = inputFile.getAbsolutePath().replaceAll("\\\\", "/");
        String fileOutputAbsolutePath = fileInputAbsolutePath + ".ftl";

        //文件输入输出相对路劲
        String fileInputPath = fileInputAbsolutePath.replace(sourceRootPath + "/", "");
        String fileOutputPath = fileInputPath + ".ftl";
        String fileContent;
        boolean hasTemplateFile = FileUtil.exist(fileOutputAbsolutePath);
        if (hasTemplateFile) {
            fileContent = FileUtil.readUtf8String(fileOutputAbsolutePath);
        } else {
            fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        }
        //支持单个和多个模型，多次替换，有分组要多一个层级
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        String repalcement;
        String newFileContent = fileContent;

        for (TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig : templateMakerModelConfig.getModels()) {
            //传入的分组配为空，无分组直接替换
            if (modelGroupConfig == null) {
                repalcement = String.format("${%s}", modelInfoConfig.getFieldName());
            } else {
                //存在分组，多替换一个层级
                repalcement = String.format("${%s.%s}", modelGroupConfig.getGroupKey(), modelInfoConfig.getFieldName());
            }
            //多次替换
            newFileContent = StrUtil.replace(newFileContent, modelInfoConfig.getReplaceText(), repalcement);
        }

        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(fileOutputPath);
        fileInfo.setOutputPath(fileInputPath);
        fileInfo.setCondition(fileInfoConfig.getCondition());
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
        boolean contentEquals = fileContent.equals(newFileContent);
        if (!hasTemplateFile) {
            if (contentEquals) {
                fileInfo.setInputPath(fileInputPath);
                fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
            } else {
                FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
            }
        } else if (!contentEquals) {
            FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
        }
        return fileInfo;
    }
}
