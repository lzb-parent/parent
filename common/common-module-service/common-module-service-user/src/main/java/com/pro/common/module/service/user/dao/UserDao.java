package com.pro.common.module.service.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pro.common.module.api.user.model.db.User;

public interface UserDao<T extends User> extends BaseMapper<T> {
}
