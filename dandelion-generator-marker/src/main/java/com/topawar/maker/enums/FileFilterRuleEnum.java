package com.topawar.maker.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

/**
 * 文件过滤规则
 * @author topawar
 */
@Getter
public enum FileFilterRuleEnum {
    CONTAINS("包含","contains"),
    REGEX("正则","regex"),
    EQUAL("相等","equal"),
    START_WITH("前缀","startWith"),
    END_WITH("后缀","endWith");

    private String text;
    private String value;

    FileFilterRuleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public static FileFilterRuleEnum getEnumByValue(String value){
        if (ObjectUtil.isEmpty(value)){
            return null;
        }
        for (FileFilterRuleEnum anEnum : FileFilterRuleEnum.values()) {
            if (anEnum.value.equals(value)){
                return anEnum;
            }
        }
        return null;
    }
}
