package com.topawar.web.job;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.topawar.web.manager.CosManager;
import com.topawar.web.mapper.GeneratorMapper;
import com.topawar.web.model.entity.Generator;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author topawar
 */
@Component
@Slf4j
public class ClearCosJobHandler {

    @Resource
    private CosManager cosManager;

    @Resource
    private GeneratorMapper generatorMapper;

    @XxlJob("clearCosJobHandler")
    public void clearCosJobHandler() {
        log.info("clearCosJobHandler start");
        //用户已制作完成的产物包路径
        cosManager.deleteDir("generator_make_template/");
        //删除已删除的额产物包路径
        QueryWrapper<Generator> generatorQueryWrapper = new QueryWrapper<>();
        generatorQueryWrapper.select("id,distPath");
        generatorQueryWrapper.eq("isDelete","1");
        List<Generator> generatorList = generatorMapper.selectList(generatorQueryWrapper);
        //移除/前缀
        List<String> keyList = generatorList.stream()
                .map(Generator::getDistPath)
                .filter(StrUtil::isNotBlank)
                .map(distPath -> distPath.substring(1))
                .collect(Collectors.toList());
        cosManager.deleteObjects(keyList);
        log.info("clearCosJobHandler end");
    }
}
