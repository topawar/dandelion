package com.topawar.maker.template;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import com.topawar.maker.enums.FileFilterRangeEnum;
import com.topawar.maker.enums.FileFilterRuleEnum;
import com.topawar.maker.model.FileFilterConfig;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author topawar
 */
public class FileFilter {

    /**
     * @param fileFilterConfigList 过滤规则
     * @param file                 单个文件
     * @return 是否保留
     */
    private static boolean doSingleFileFilter(List<FileFilterConfig> fileFilterConfigList, File file) {
        //获取文件名称和文件内容
        String fileName = file.getName();
        String fileContent = FileUtil.readUtf8String(file);

        //所有过滤器校验结束的结果
        boolean result = true;
        if (CollectionUtil.isEmpty(fileFilterConfigList)) {
            return true;
        }

        for (FileFilterConfig fileFilterConfig : fileFilterConfigList) {
            String range = fileFilterConfig.getRange();
            String rule = fileFilterConfig.getRule();
            String value = fileFilterConfig.getValue();
            //获取过滤范围
            FileFilterRangeEnum filterRangeEnum = FileFilterRangeEnum.getEnumByValue(range);
            if (filterRangeEnum == null) {
                continue;
            }
            //要过滤的原内容
            String content = fileName;
            switch (filterRangeEnum) {
                case FILE_NANE:
                    content = fileName;
                    break;
                case FILE_CONTENT:
                    content = fileContent;
                    break;
                default:
            }

            FileFilterRuleEnum filterRuleEnum = FileFilterRuleEnum.getEnumByValue(rule);
            switch (filterRuleEnum) {
                case CONTAINS:
                    result = content.contains(value);
                    break;
                case REGEX:
                    result = content.matches(value);
                    break;
                case EQUAL:
                    result = content.equals(value);
                    break;
                case START_WITH:
                    result = content.startsWith(value);
                    break;
                case END_WITH:
                    result = content.endsWith(value);
                    break;
                default:
            }
            //不满足任意条件返回false
            if (!result) {
                return false;
            }
        }
        return true;
    }

    /**
     * 对文件或文件目录进行过滤返回文件列表
     *
     * @param filePath
     * @param fileFilterConfigList
     * @return
     */
    public static List<File> doFileFilter(String filePath, List<FileFilterConfig> fileFilterConfigList) {
        List<File> loopFiles = FileUtil.loopFiles(filePath);
        return loopFiles.stream().filter(file -> doSingleFileFilter(fileFilterConfigList, file))
                .collect(Collectors.toList());
    }
}
