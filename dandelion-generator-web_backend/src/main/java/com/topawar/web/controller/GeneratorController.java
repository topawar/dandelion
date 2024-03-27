package com.topawar.web.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.topawar.maker.generator.GenerateTemplate;
import com.topawar.maker.generator.ZipGenerator;
import com.topawar.maker.meta.Meta;
import com.topawar.maker.meta.MetaValidator;
import com.topawar.web.annotation.AuthCheck;
import com.topawar.web.common.BaseResponse;
import com.topawar.web.common.ErrorCode;
import com.topawar.web.common.ResultUtils;
import com.topawar.web.constant.UserConstant;
import com.topawar.web.exception.BusinessException;
import com.topawar.web.exception.ThrowUtils;
import com.topawar.web.manager.CacheManager;
import com.topawar.web.manager.CosManager;
import com.topawar.web.model.dto.generator.*;
import com.topawar.web.model.entity.Generator;
import com.topawar.web.model.entity.User;
import com.topawar.web.model.vo.GeneratorVO;
import com.topawar.web.service.GeneratorService;
import com.topawar.web.service.UserService;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 帖子接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/Generator")
@Slf4j
public class GeneratorController {

    @Resource
    private GeneratorService generatorService;

    @Resource
    private UserService userService;

    @Resource
    private CosManager cosManager;

    @Resource
    private CacheManager cacheManager;

    // region 增删改查

    /**
     * 创建
     *
     * @param GeneratorAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addGenerator(@RequestBody GeneratorAddRequest GeneratorAddRequest, HttpServletRequest request) {
        if (GeneratorAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Generator Generator = new Generator();
        BeanUtils.copyProperties(GeneratorAddRequest, Generator);
        List<String> tags = GeneratorAddRequest.getTags();
        if (tags != null) {
            Generator.setTags(JSONUtil.toJsonStr(tags));
        }
        User loginUser = userService.getLoginUser(request);
        Generator.setUserId(loginUser.getId());
        boolean result = generatorService.save(Generator);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newGeneratorId = Generator.getId();
        return ResultUtils.success(newGeneratorId);
    }

//    /**
//     * 删除
//     *
//     * @param deleteRequest
//     * @param request
//     * @return
//     */
//    @PostMapping("/delete")
//    public BaseResponse<Boolean> deleteGenerator(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
//        if (deleteRequest == null || deleteRequest.getId() <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        User user = userService.getLoginUser(request);
//        long id = deleteRequest.getId();
//        // 判断是否存在
//        Generator oldGenerator = GeneratorService.getById(id);
//        ThrowUtils.throwIf(oldGenerator == null, ErrorCode.NOT_FOUND_ERROR);
//        // 仅本人或管理员可删除
//        if (!oldGenerator.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
//        boolean b = GeneratorService.removeById(id);
//        return ResultUtils.success(b);
//    }

    /**
     * 更新（仅管理员）
     *
     * @param GeneratorUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateGenerator(@RequestBody GeneratorUpdateRequest GeneratorUpdateRequest) {
        if (GeneratorUpdateRequest == null || GeneratorUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Generator Generator = new Generator();
        BeanUtils.copyProperties(GeneratorUpdateRequest, Generator);
        List<String> tags = GeneratorUpdateRequest.getTags();
        if (tags != null) {
            Generator.setTags(JSONUtil.toJsonStr(tags));
        }
        // 参数校验
//        GeneratorService.validGenerator(Generator, false);
        long id = GeneratorUpdateRequest.getId();
        // 判断是否存在
        Generator oldGenerator = generatorService.getById(id);
        ThrowUtils.throwIf(oldGenerator == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = generatorService.updateById(Generator);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<GeneratorVO> getGeneratorVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Generator Generator = generatorService.getById(id);
        if (Generator == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(generatorService.getGeneratorVO(Generator, request));
    }


    /**
     * 编辑（用户）
     *
     * @param generatorEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editGenerator(@RequestBody GeneratorEditRequest generatorEditRequest, HttpServletRequest request) {
        if (generatorEditRequest == null || generatorEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Generator generator = new Generator();
        BeanUtils.copyProperties(generatorEditRequest, generator);
        List<String> tags = generatorEditRequest.getTags();
        generator.setTags(JSONUtil.toJsonStr(tags));
        Meta.FileConfig fileConfig = generatorEditRequest.getFileConfig();
        generator.setFileConfig(JSONUtil.toJsonStr(fileConfig));
        Meta.ModelConfig modelConfig = generatorEditRequest.getModelConfig();
        generator.setModelConfig(JSONUtil.toJsonStr(modelConfig));

        // 参数校验
        generatorService.validGenerator(generator, false);
        User loginUser = userService.getLoginUser(request);
        long id = generatorEditRequest.getId();
        // 判断是否存在
        Generator oldGenerator = generatorService.getById(id);
        ThrowUtils.throwIf(oldGenerator == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldGenerator.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = generatorService.updateById(generator);
        return ResultUtils.success(result);
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param GeneratorQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Generator>> listGeneratorByPage(@RequestBody GeneratorQueryRequest GeneratorQueryRequest) {
        long current = GeneratorQueryRequest.getCurrent();
        long size = GeneratorQueryRequest.getPageSize();
        Page<Generator> GeneratorPage = generatorService.page(new Page<>(current, size),
                generatorService.getQueryWrapper(GeneratorQueryRequest));
        return ResultUtils.success(GeneratorPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param GeneratorQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<GeneratorVO>> listGeneratorVOByPage(@RequestBody GeneratorQueryRequest GeneratorQueryRequest,
                                                                 HttpServletRequest request) {
        long current = GeneratorQueryRequest.getCurrent();
        long size = GeneratorQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Generator> GeneratorPage = generatorService.page(new Page<>(current, size),
                generatorService.getQueryWrapper(GeneratorQueryRequest));
        return ResultUtils.success(generatorService.getGeneratorVOPage(GeneratorPage, request));
    }

    @PostMapping("/list/page/fast/vo")
    public BaseResponse<Page<GeneratorVO>> listGeneratorVOByPageFast(@RequestBody GeneratorQueryRequest generatorQueryRequest,
                                                                     HttpServletRequest request) {
        long current = generatorQueryRequest.getCurrent();
        long size = generatorQueryRequest.getPageSize();
        String key = getCacheKey(generatorQueryRequest);
        Object cacheValue = cacheManager.get(key);
        if (null != cacheValue) {
//            return ResultUtils.success(JSONUtil.toBean(cacheValue, new TypeReference<Page<GeneratorVO>>() {
//            }, false));
            return ResultUtils.success((Page<GeneratorVO>) cacheValue);
        }
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        QueryWrapper<Generator> queryWrapper = generatorService.getQueryWrapper(generatorQueryRequest);
        queryWrapper.select("id", "name", "description", "author", "picture","tags","createTime");
        Page<Generator> generatorPage = generatorService.page(new Page<>(current, size),
                queryWrapper);
        cacheManager.put(key, generatorPage);
        return ResultUtils.success(generatorService.getGeneratorVOPage(generatorPage, request));
    }

    /**
     * 生成缓存key
     *
     * @param generatorQueryRequest
     * @return
     */
    @NotNull
    private static String getCacheKey(GeneratorQueryRequest generatorQueryRequest) {
        String jsonStr = JSONUtil.toJsonStr(generatorQueryRequest);
        String base64 = Base64.encode(jsonStr);
        String key = "dandelion:page:query:" + base64;
        return key;
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param GeneratorQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<GeneratorVO>> listMyGeneratorVOByPage(@RequestBody GeneratorQueryRequest GeneratorQueryRequest,
                                                                   HttpServletRequest request) {
        if (GeneratorQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        GeneratorQueryRequest.setUserId(loginUser.getId());
        long current = GeneratorQueryRequest.getCurrent();
        long size = GeneratorQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Generator> GeneratorPage = generatorService.page(new Page<>(current, size),
                generatorService.getQueryWrapper(GeneratorQueryRequest));
        return ResultUtils.success(generatorService.getGeneratorVOPage(GeneratorPage, request));
    }


    /**
     * 使用代码生成器
     *
     * @param generatorUseRequest
     * @param request
     * @param response
     */
    @PostMapping("/use")
    public void useGenerator(@RequestBody GeneratorUseRequest generatorUseRequest,
                             HttpServletRequest request,
                             HttpServletResponse response) throws IOException, InterruptedException {
        User loginUser = userService.getLoginUser(request);
        if (null == loginUser) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "必须登录，才能使用");
        }

        Long id = generatorUseRequest.getId();
        Map<String, Object> dataModel = generatorUseRequest.getDataModel();
        Generator generator = generatorService.getById(id);
        if (null == generator) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        //生成器存储路径
        String distPath = generator.getDistPath();
        if (StrUtil.isBlank(distPath)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "产物包不存在");
        }
        String cacheFile = generatorService.getCacheFile(id);
        //临时工作空间
        String projectPath = System.getProperty("user.dir");
        String tempDirPath = String.format("%s/.temp/use/%s", projectPath, id);
        String zipFilePath = null;
        if (StrUtil.isNotBlank(cacheFile)) {
            log.info("cache不为空，通过缓存下载");
            zipFilePath = cacheFile;
        } else {
            //压缩包文件路径
            zipFilePath = tempDirPath + "/dist.zip";
            if (!FileUtil.exist(zipFilePath)) {
                FileUtil.touch(zipFilePath);
            }
            //下载压缩包文件并解压
            try {
                cosManager.download(distPath, zipFilePath);
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
        File unzipDir = ZipUtil.unzip(zipFilePath);
        String modelJsonPath = tempDirPath + "/DataModel.json";
        FileUtil.writeUtf8String(JSONUtil.toJsonStr(dataModel), modelJsonPath);
        File generatorExec = FileUtil.loopFiles(unzipDir, 2, null).stream()
                .filter(file -> file.isFile() && "exec".equals(file.getName()))
                .findFirst()
                .orElseThrow(RuntimeException::new);
        //赋予可执行权限
        try {
//            Set<PosixFilePermission> posixFilePermissions = PosixFilePermissions.fromString("rwxrwxrwx");
//            Files.setPosixFilePermissions(generatorExec.toPath(), posixFilePermissions);
            generatorExec.setReadable(true);
            generatorExec.setWritable(true);
            generatorExec.setExecutable(true);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "分配权限失败");
        }

        //获取脚本执行路径，window下需要转义
        File execParentFile = generatorExec.getParentFile();
//        String tranPath = generatorExec.getAbsolutePath().replace("\\", "/");
        String tranPath = generatorExec.getAbsolutePath();
        //传入转义后的路径
        String[] commands = new String[]{tranPath, "json-generate", "--file=" + modelJsonPath};

        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.directory(execParentFile);
        try {
            Process process = processBuilder.start();
            InputStream processInputStream = process.getInputStream();
            //读取日志
            BufferedReader reader = new BufferedReader(new InputStreamReader(processInputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            //等待命令完成
            int exitCode = process.waitFor();
            System.out.println("命令执行完成：退出码：" + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //返回生成结果的压缩包
        String generatePath = execParentFile.getAbsolutePath() + "/generated";
        String resultPath = tempDirPath + "/result.zip";
        File zip = ZipUtil.zip(generatePath, resultPath);
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + zip.getName());
        //写入响应，jdk自带
        Files.copy(zip.toPath(), response.getOutputStream());
        //异步清理文件
        CompletableFuture.runAsync(() -> {
            FileUtil.del(tempDirPath);
        });
    }

    @PostMapping("/make")
    public void generatorMaker(@RequestBody GeneratorMakerRequest generatorMakerRequest
            , HttpServletRequest request, HttpServletResponse response) throws TemplateException, IOException, InterruptedException {

        User loginUser = userService.getLoginUser(request);
        if (null == loginUser) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "必须登录，才能使用");
        }
        String zipPath = generatorMakerRequest.getZipPath();
        Meta meta = generatorMakerRequest.getMeta();
        if (StrUtil.isBlank(zipPath)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "资源包不存在");
        }
        Long id = loginUser.getId();
        String projectPath = System.getProperty("user.dir");
        String tempDirPath = String.format("%s/.temp/make/%s", projectPath, id);

        //压缩包文件路径
        String localZipFilePath = tempDirPath + "/project.zip";
        if (!FileUtil.exist(localZipFilePath)) {
            FileUtil.touch(localZipFilePath);
        }

        //下载压缩包文件并解压
        try {
            cosManager.download(zipPath, localZipFilePath);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        File unzipDir = ZipUtil.unzip(localZipFilePath);
        String sourceRootPath = unzipDir.getAbsolutePath();
        meta.getFileConfig().setSourceRootPath(sourceRootPath);
        MetaValidator.doValidAndFill(meta);
        String outputPath = String.format("%s/generated/%s", tempDirPath, meta.getName());
        //调用maker项目制作生成器
        GenerateTemplate zipGenerator = new ZipGenerator();
        zipGenerator.doGenerate(meta, outputPath);
        //打包生成好代码生成器
        String suffix = "-dist.zip";
        String zipFileName = meta.getName() + suffix;
        String distZipPath = outputPath + suffix;

        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipFileName);

        Files.copy(Paths.get(distZipPath), response.getOutputStream());
        //异步清理文件
        CompletableFuture.runAsync(() -> {
            FileUtil.del(tempDirPath);
        });
    }

}
