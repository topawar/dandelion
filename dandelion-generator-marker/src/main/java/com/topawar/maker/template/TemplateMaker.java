package com.topawar.maker.template;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.topawar.maker.enums.FileFilterRangeEnum;
import com.topawar.maker.enums.FileFilterRuleEnum;
import com.topawar.maker.enums.FileGenerateTypeEnum;
import com.topawar.maker.enums.FileTypeEnum;
import com.topawar.maker.meta.Meta;
import com.topawar.maker.model.FileFilterConfig;
import com.topawar.maker.model.TemplateMakerFileConfig;
import com.topawar.maker.model.TemplateMakerModelConfig;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
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

    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        String originProjectPath = new File(projectPath).getParent() + File.separator + "dandelion-generator-demo-projects/springboot-init";
        String inputFilePath1 = "src/main/java/com/yupi/springbootinit/common";
//        String inputFilePath2 = "src/main/java/com/yupi/springbootinit/controller";
        String inputFilePath2 = "src/main/resources/application.yml";

        Meta meta = new Meta();
        meta.setName("acm-template-generator");
        meta.setDescription("ACM 示例模板生成器");

        // 模型参数配置
        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();

        // - 模型组配置
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = new TemplateMakerModelConfig.ModelGroupConfig();
        modelGroupConfig.setGroupKey("mysql");
        modelGroupConfig.setGroupName("数据库配置");
//        templateMakerModelConfig.setModelGroupConfig(modelGroupConfig);

        // - 模型配置
        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
//        modelInfoConfig1.setFieldName("url");
//        modelInfoConfig1.setType("String");
//        modelInfoConfig1.setDefaultValue("jdbc:mysql://localhost:3306/my_db");
//        modelInfoConfig1.setReplaceText("jdbc:mysql://localhost:3306/my_db");
//
//        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig2 = new TemplateMakerModelConfig.ModelInfoConfig();
//        modelInfoConfig2.setFieldName("username");
//        modelInfoConfig2.setType("String");
//        modelInfoConfig2.setDefaultValue("root");
//        modelInfoConfig2.setReplaceText("root");

        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig2 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig2.setFieldName("className");
        modelInfoConfig2.setType("String");
        modelInfoConfig2.setDefaultValue("BaseResponse");
        modelInfoConfig2.setReplaceText("BaseResponse");

        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigList = Arrays.asList(modelInfoConfig1, modelInfoConfig2);
        templateMakerModelConfig.setModelInfoConfigs(modelInfoConfigList);


        // 文件过滤
        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig1.setPath(inputFilePath1);
        List<FileFilterConfig> fileFilterConfigList = new ArrayList<>();
        FileFilterConfig fileFilterConfig = FileFilterConfig.builder()
                .range(FileFilterRangeEnum.FILE_NANE.getValue())
                .rule(FileFilterRuleEnum.CONTAINS.getValue())
                .value("Base")
                .build();
        fileFilterConfigList.add(fileFilterConfig);
        fileInfoConfig1.setFileFilterConfigList(fileFilterConfigList);

        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig2 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig2.setPath(inputFilePath2);
        templateMakerFileConfig.setFileInfoConfigList(Arrays.asList(fileInfoConfig1, fileInfoConfig2));

        // 分组配置
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = new TemplateMakerFileConfig.FileGroupConfig();
        fileGroupConfig.setCondition("outputText");
        fileGroupConfig.setGroupKey("test");
        fileGroupConfig.setGroupName("测试分组");
        templateMakerFileConfig.setFileGroupConfig(fileGroupConfig);

        long id = makeTemplate(meta, originProjectPath, templateMakerFileConfig, templateMakerModelConfig, 1763243844935725056L);
        System.out.println(id);
    }

    private static long makeTemplate(Meta newMeta, String originProjectPath, TemplateMakerFileConfig templateFileConfig, TemplateMakerModelConfig templateMakerModelConfig, Long id) {
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

        String sourceRootPath = templatePath + File.separator + FileUtil.getLastPathEle(Paths.get(originProjectPath)).toString();
        // 注意 win 系统需要对路径进行转义
        sourceRootPath = sourceRootPath.replaceAll("\\\\", "/");

        //文件模板生成，如果是文件夹向下遍历
        List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
        List<TemplateMakerFileConfig.FileInfoConfig> fileInfoConfigList = templateFileConfig.getFileInfoConfigList();
        for (TemplateMakerFileConfig.FileInfoConfig fileInfoConfig : fileInfoConfigList) {

            String inputfilePath = fileInfoConfig.getPath();
            //如果是相对路径要转换为绝对路局
            if (!inputfilePath.startsWith(sourceRootPath)) {
                inputfilePath = sourceRootPath + File.separator + inputfilePath;
            }

            //获取过滤后的文件列表
            List<File> fileList = FileFilter.doFileFilter(inputfilePath, fileInfoConfig.getFileFilterConfigList());
            fileList = fileList.stream().filter(file -> !file.getAbsolutePath().endsWith(".ftl")).collect(Collectors.toList());
            for (File file : fileList) {
                Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(templateMakerModelConfig, sourceRootPath, file);
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
        //模型列表
        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigList = templateMakerModelConfig.getModelInfoConfigs();
        List<Meta.ModelConfig.ModelInfo> modelInfoList = modelInfoConfigList.stream().map(modelInfoConfig -> {
            Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
            BeanUtil.copyProperties(modelInfoConfig, modelInfo);
            return modelInfo;
        }).collect(Collectors.toList());
        List<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>();
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        if (modelGroupConfig != null) {
            String groupName = modelGroupConfig.getGroupName();
            String groupKey = modelGroupConfig.getGroupKey();
            String condition = modelGroupConfig.getCondition();
            Meta.ModelConfig.ModelInfo groupModelInfo = new Meta.ModelConfig.ModelInfo();
            groupModelInfo.setGroupKey(groupKey);
            groupModelInfo.setGroupName(groupName);
            groupModelInfo.setCondition(condition);
            groupModelInfo.setModels(modelInfoList);
            newModelInfoList.add(groupModelInfo);
        } else {
            newModelInfoList.addAll(modelInfoList);
        }

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
        //生成元数据信息文件
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(newMeta), fileMetaOutputPath);
        return id;
    }

    /**
     * 制作文件模板
     *
     * @param sourceRootPath
     * @param inputFile
     * @return
     */
    private static Meta.FileConfig.FileInfo makeFileTemplate(TemplateMakerModelConfig templateMakerModelConfig, String sourceRootPath, File inputFile) {

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

        for (TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig : templateMakerModelConfig.getModelInfoConfigs()) {
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
