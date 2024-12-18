package com.pro.common.modules.api.dependencies.model.classes;



import java.time.LocalDateTime;
import com.pro.framework.api.model.IModel;

/**
 * 用户类
 * 用户只能访问自己的,代理只能访问自己下级的
 */
public interface IUserClass {
    Long getUserId();
    void setUserId(Long userId);
//    LocalDateTime getCreateTime();
}
