package com.yupi.springbootinit.common;

import java.io.Serializable;
import lombok.Data;

/**
 * 通用返回类
 *
 * @param <T>
 */
@Data
public class ${outputText}<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    public ${outputText}(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public ${outputText}(int code, T data) {
        this(code, data, "");
    }

    public ${outputText}(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
