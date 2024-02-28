package com.topawar.maker.template;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
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
import com.topawar.maker.model.TemplateFileMakerConfig;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author topawar
 */
public class TemplateMaker {
//    public static void main(String[] args) {
//
//        //输入文件信息
//        String projectPath = System.getProperty("user.dir");
//        String inputPath = new File(projectPath).getParent() + File.separator + "dandelion-generator-demo-projects/acm-template";
//        // 注意 win 系统需要对路径进行转义
//        inputPath = inputPath.replaceAll("\\\\", "/");
//
//        //复制目录
//        long id = IdUtil.getSnowflakeNextId();
//        String tempDir = projectPath + File.separator + ".temp";
//        String templatePath = tempDir + File.separator + id;
//        if (!FileUtil.exist(templatePath)) {
//            FileUtil.mkdir(templatePath);
//        }
//        FileUtil.copy(inputPath, templatePath, false);
//
//        //输入项目基本信息
//        String name = "acm-template-generator";
//        String description = "acm模板生成器";
//
//        String fileInputPath = "src/com/yupi/acm/MainTemplate.java";
//        String fileOutputPath = "src/com/yupi/acm/MainTemplate.java" + ".ftl";
//        String sourceRootPath = templatePath + File.separator + FileUtil.getLastPathEle(Paths.get(inputPath)).toString();
//
//        //输入模型参数信息
//        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
//        modelInfo.setType("String");
//        modelInfo.setFieldName("outputText");
//        modelInfo.setDefaultValue("sum=:");
//
//        //生成模板文件
//        String templateFileInput = sourceRootPath + File.separator + fileInputPath;
//        String fileContent = FileUtil.readUtf8String(templateFileInput);
//        String repalcement = String.format("${%s}", modelInfo.getFieldName());
//        String newFileContent = StrUtil.replace(fileContent, "Sum: ", repalcement);
//
//        String templateFileOutputPath = sourceRootPath + File.separator + fileOutputPath;
//        FileUtil.writeUtf8String(newFileContent, templateFileOutputPath);
//
//        //生成meta.json文件
//        String fileMetaOutputPath = sourceRootPath + File.separator + "meta.json";
//        Meta meta = new Meta();
//        meta.setName(name);
//        meta.setDescription(description);
//        //注入模型信息
//        Meta.ModelConfig modelConfig = new Meta.ModelConfig();
//        meta.setModelConfig(modelConfig);
//        ArrayList<Meta.ModelConfig.ModelInfo> modelInfos = new ArrayList<>();
//        modelInfos.add(modelInfo);
//        modelConfig.setModels(modelInfos);
//
//        //注入文件配置信息
//        Meta.FileConfig fileConfig = new Meta.FileConfig();
//        meta.setFileConfig(fileConfig);
//        fileConfig.setSourceRootPath(sourceRootPath);
//        ArrayList<Meta.FileConfig.FileInfo> fileInfos = new ArrayList<>();
//        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
//        fileInfo.setInputPath(fileInputPath);
//        fileInfo.setOutputPath(fileOutputPath);
//        fileInfo.setType(FileTypeEnum.FILE.getValue());
//        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
//        fileInfos.add(fileInfo);
//        fileConfig.setFiles(fileInfos);
//        //生成元数据信息文件
//        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(meta), fileMetaOutputPath);
//    }

    private static List<Meta.FileConfig.FileInfo> distinctFiles(List<Meta.FileConfig.FileInfo> fileInfoList) {
        return new ArrayList<>(fileInfoList
                .stream()
                .collect(Collectors
                        .toMap(Meta.FileConfig.FileInfo::getInputPath, o -> o, (e, r) -> r)).values());
    }

    private static List<Meta.ModelConfig.ModelInfo> distinctModels(List<Meta.ModelConfig.ModelInfo> modelInfoList) {
        return new ArrayList<>(modelInfoList
                .stream()
                .collect(Collectors
                        .toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r)).values());
    }

    //    public static void main(String[] args) {
//        Meta meta = new Meta();
//        meta.setName("acm-template-generator");
//        meta.setDescription("ACM 示例模板生成器");
//
//        String projectPath = System.getProperty("user.dir");
//        String originProjectPath = new File(projectPath).getParent() + File.separator + "dandelion-generator-demo-projects/acm-template";
//        String inputFilePath = "src/com/yupi/acm/MainTemplate.java";
//
//
//        // 模型参数信息（首次）
////        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
////        modelInfo.setFieldName("outputText");
////        modelInfo.setType("String");
////        modelInfo.setDefaultValue("sum = ");
//
//        // 模型参数信息（第二次）
//        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
//        modelInfo.setFieldName("className");
//        modelInfo.setType("String");
//
//        // 替换变量（首次）
////        String searchStr = "Sum: ";
//        // 替换变量（第二次）
//        String searchStr = "MainTemplate";
//
////        long id = makeTemplate(meta, originProjectPath, inputFilePath, modelInfo, searchStr, null);
//        long id = makeTemplate(meta, originProjectPath, inputFilePath, modelInfo, searchStr, 1762756789092864000L);
////        System.out.println(id);
//
//    }
    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        String originProjectPath = new File(projectPath).getParent() + File.separator + "dandelion-generator-demo-projects/springboot-init";
        String inputFilePath = "/src/main/java/com/yupi/springbootinit";
        Meta meta = new Meta();
        meta.setName("acm-template-generator");
        meta.setDescription("ACM 示例模板生成器");
//        List<String> list = new ArrayList<>();
//        list.add(inputFilePath);
        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        modelInfo.setFieldName("outputText");
        modelInfo.setType("String");
        modelInfo.setDefaultValue("sum = ");
        TemplateFileMakerConfig templateFileMakerConfig = new TemplateFileMakerConfig();
        TemplateFileMakerConfig.FileInfoConfig fileInfoConfig = new TemplateFileMakerConfig.FileInfoConfig();
        fileInfoConfig.setPath(inputFilePath);
        List<FileFilterConfig> fileFilterConfigList= new ArrayList<>();
        FileFilterConfig fileFilterConfig = new FileFilterConfig();
        fileFilterConfig.setRule(FileFilterRuleEnum.CONTAINS.getValue());
        fileFilterConfig.setRange(FileFilterRangeEnum.FILE_NANE.getValue());
        fileFilterConfig.setValue("Base");
        fileFilterConfigList.add(fileFilterConfig);
        fileInfoConfig.setFileFilterConfigList(fileFilterConfigList);
        ArrayList<TemplateFileMakerConfig.FileInfoConfig> fileInfoConfigs = new ArrayList<>();
        fileInfoConfigs.add(fileInfoConfig);
        templateFileMakerConfig.setFileInfoConfigList(fileInfoConfigs);
        makeTemplate(meta, originProjectPath, templateFileMakerConfig, modelInfo, "BaseResponse", null);
    }

    private static long makeTemplate(Meta newMeta, String originProjectPath, TemplateFileMakerConfig templateFileConfig, Meta.ModelConfig.ModelInfo modelInfo, String searchStr, Long id) {
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

        //文件模板生成，如果是文件夹向下遍历
        List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
        List<TemplateFileMakerConfig.FileInfoConfig> fileInfoConfigList = templateFileConfig.getFileInfoConfigList();
        for (TemplateFileMakerConfig.FileInfoConfig fileInfoConfig : fileInfoConfigList) {

            String inputfilePath = fileInfoConfig.getPath();
            //如果是相对路径要转换为绝对路局
            if (!inputfilePath.startsWith(sourceRootPath)) {
                inputfilePath = sourceRootPath + File.separator + inputfilePath;
            }

            //获取过滤后的文件列表
//            if (FileUtil.isDirectory(inputfilePath)) {
//                List<File> files = FileUtil.loopFiles(inputfilePath);
//                for (File file : files) {
//                    Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(modelInfo, searchStr, sourceRootPath, file);
//                    fileInfoList.add(fileInfo);
//                }
//            } else {
//                Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(modelInfo, searchStr, sourceRootPath, new File(inputfilePath));
//                fileInfoList.add(fileInfo);
//            }
            List<File> fileList = FileFilter.doFileFilter(inputfilePath, fileInfoConfig.getFileFilterConfigList());
            for (File file : fileList) {
                Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(modelInfo, searchStr, sourceRootPath, file);
                fileInfoList.add(fileInfo);
            }
        }


        //生成meta.json文件
        String fileMetaOutputPath = sourceRootPath + File.separator + "meta.json";
        if (FileUtil.exist(fileMetaOutputPath)) {
            Meta oldMeta = JSONUtil.toBean(FileUtil.readUtf8String(fileMetaOutputPath), Meta.class);
            BeanUtil.copyProperties(oldMeta, newMeta, CopyOptions.create().ignoreNullValue());
            newMeta = oldMeta;
            //去重
            List<Meta.FileConfig.FileInfo> fileInfos = newMeta.getFileConfig().getFiles();
            List<Meta.FileConfig.FileInfo> distinctFiles = distinctFiles(fileInfos);
            List<Meta.ModelConfig.ModelInfo> modelInfos = newMeta.getModelConfig().getModels();
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
            modelInfos.add(modelInfo);
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
     * @param modelInfo
     * @param searchText
     * @param sourceRootPath
     * @param inputFile
     * @return
     */
    private static Meta.FileConfig.FileInfo makeFileTemplate(Meta.ModelConfig.ModelInfo modelInfo, String searchText, String sourceRootPath, File inputFile) {

        // 注意 win 系统需要对路径进行转义
        String fileInputAbsolutePath = inputFile.getAbsolutePath().replaceAll("\\\\", "/");
        String fileOutputAbsolutePath = fileInputAbsolutePath + ".ftl";

        //文件输入输出相对路劲
        String fileInputPath = fileInputAbsolutePath.replace(fileOutputAbsolutePath + "/", "");
        String fileOutputPath = fileInputPath + ".ftl";
        String fileContent;
        if (FileUtil.exist(fileOutputPath)) {
            fileContent = FileUtil.readUtf8String(fileOutputAbsolutePath);
        } else {
            fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        }
        String repalcement = String.format("${%s}", modelInfo.getFieldName());
        String newFileContent = StrUtil.replace(fileContent, searchText, repalcement);
        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(fileInputPath);
        fileInfo.setOutputPath(fileOutputPath);
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
        if (fileContent.equals(newFileContent)) {
            fileInfo.setOutputPath(fileOutputPath);
            fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
        } else {
            FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
        }
        return fileInfo;
    }
}
