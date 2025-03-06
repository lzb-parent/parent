package com.pro.common.modules.api.dependencies.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pro.common.modules.api.dependencies.model.IResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException implements IResponse<Void> {
    private static final long serialVersionUID = 1000000L;
    private Integer code;
    private String msg;
    private Object[] params;

    public BusinessException(String msg) {
        this(500, msg, (Object[]) null);
    }

    public BusinessException(String msg, Object... params) {
        this(500, msg, params);
    }

    public BusinessException(Integer code, String msg, Object... params) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.params = params;
    }

    @Override
    public Void getData() {
        return null;
    }

//    @JsonIgnore
//    @Override
//    public StackTraceElement[] getStackTrace() {
//        return new StackTraceElement[0];  // 返回空的堆栈跟踪
//    }

//    @JsonIgnore
//    @Override
//    public String getLocalizedMessage() {
//        return super.getLocalizedMessage();
//    }
}
