package com.topawar.maker.meta;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author topawar
 */
public class MetaManager {
    private static volatile Meta meta;

    private MetaManager() {
        //私有构造函数，防止外部实例化
    }

    public static Meta getMetaObject() {
        if (meta == null) {
            synchronized (Meta.class) {
                if (meta == null) {
                    return initMeta();
                }
            }
        }
        return null;
    }

    private static Meta initMeta() {
        String metaJson = ResourceUtil.readUtf8Str("springboot-init.json");
        Meta newMeta = JSONUtil.toBean(metaJson, Meta.class);
        //校验数据，填充非必填默认值
        MetaValidator.doValidAndFill(newMeta);
        return newMeta;
    }
}
