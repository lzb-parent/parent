package com.pro.common.modules.api.dependencies.model.classes;


import com.pro.framework.api.model.IModel;

/**
 * 开放配置类
 *     user   agent  admin  代码逻辑
 * 增    0      0      1       1
 * 删    0      0      1       1
 * 改    0      0      1       1
 * 查    1      1      1       1
 */
public interface IOpenConfigClass extends IModel {
    Boolean getEnabled();
    Integer getSort();
}
