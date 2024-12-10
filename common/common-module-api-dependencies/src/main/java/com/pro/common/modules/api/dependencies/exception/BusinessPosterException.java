package com.pro.common.modules.api.dependencies.exception;

import lombok.Getter;

import java.util.Map;

/**
 * 自定义异常
 */
@Getter
public class BusinessPosterException extends BusinessException {
    private String posterCode;
    private Map<String, Object> paramMap;
    private Integer code = 20000;

    public BusinessPosterException(String posterCode) {
        super("BusinessPosterException:" + posterCode);
        this.posterCode = posterCode;
    }

    public BusinessPosterException(String posterCode, Map<String, Object> paramMap) {
        super("BusinessPosterException:" + posterCode);
        this.posterCode = posterCode;
        this.paramMap = paramMap;
    }
}
