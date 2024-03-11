package com.topawar.web.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.topawar.web.annotation.AuthCheck;
import com.topawar.web.common.BaseResponse;
import com.topawar.web.common.ErrorCode;
import com.topawar.web.common.ResultUtils;
import com.topawar.web.constant.UserConstant;
import com.topawar.web.exception.BusinessException;
import com.topawar.web.exception.ThrowUtils;
import com.topawar.web.meta.Meta;
import com.topawar.web.model.dto.generator.GeneratorAddRequest;
import com.topawar.web.model.dto.generator.GeneratorEditRequest;
import com.topawar.web.model.dto.generator.GeneratorQueryRequest;
import com.topawar.web.model.dto.generator.GeneratorUpdateRequest;
import com.topawar.web.model.entity.Generator;
import com.topawar.web.model.entity.User;
import com.topawar.web.model.vo.GeneratorVO;
import com.topawar.web.service.GeneratorService;
import com.topawar.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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


}
