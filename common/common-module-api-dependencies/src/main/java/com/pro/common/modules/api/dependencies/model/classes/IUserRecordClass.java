package com.pro.common.modules.api.dependencies.model.classes;


import com.pro.framework.api.model.IModel;

/**
 * 用户记录类
 * 开放查询,只允许代码逻辑中做新增,其他都不做增删改
 *     user   agent  admin  代码逻辑
 * 增    0      0      0       1
 * 删    0      0      0       0
 * 改    0      0      0       0
 * 查    1      1      1       1
 */
public interface IUserRecordClass extends IUserClass, IModel {
}
