package com.pro.common.modules.api.dependencies.model;

public interface IResponse<T> {
    Integer getCode();
    String getMsg();
    T getData();
}
