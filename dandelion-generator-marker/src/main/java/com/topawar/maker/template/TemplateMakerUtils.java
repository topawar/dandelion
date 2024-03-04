package com.topawar.maker.template;

import cn.hutool.core.util.StrUtil;
import com.topawar.maker.meta.Meta;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author topawar
 */
public class TemplateMakerUtils {

    /**
     * 移除未分组的同名文件
     *
     * @param fileInfoList
     * @return
     */
    public static List<Meta.FileConfig.FileInfo> removeGroupFilesFromRoot(List<Meta.FileConfig.FileInfo> fileInfoList) {
        List<Meta.FileConfig.FileInfo> groupFileInfoList = fileInfoList.stream()
                .filter(fileInfo -> StrUtil.isNotBlank(fileInfo.getGroupKey()))
                .collect(Collectors.toList());

        //获取分组类的所有文件信息
        List<Meta.FileConfig.FileInfo> groupInnerFileInfoList = groupFileInfoList.stream()
                .flatMap(fileInfo -> fileInfo.getFiles().stream()).collect(Collectors.toList());

        Set<String> fileInfoSet = groupInnerFileInfoList.stream()
                .map(Meta.FileConfig.FileInfo::getInputPath)
                .collect(Collectors.toSet());

        return fileInfoList.stream()
                .filter(fileInfo -> !fileInfoSet.contains(fileInfo.getInputPath()))
                .collect(Collectors.toList());

    }
}
