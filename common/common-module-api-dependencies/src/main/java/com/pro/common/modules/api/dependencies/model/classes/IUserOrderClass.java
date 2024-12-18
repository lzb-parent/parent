package com.pro.common.modules.api.dependencies.model.classes;


import com.pro.framework.api.model.IModel;

/**
 * 用户订单类
 * 开放查询,用户下单,后台编辑
 *     user   agent  admin  代码逻辑
 * 增    1      0      0       1
 * 删    0      0      0       1
 * 改    0      1      1       1
 * 查    1      1      1       1
 */
public interface IUserOrderClass extends IUserClass, IModel {
}
