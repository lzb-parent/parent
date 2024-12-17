package com.pro.common.module.service.usermoney.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pro.common.module.api.usermoney.model.db.UserAmountTotal;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * 资金待结算总计 Dao
 */
public interface UserAmountTotalDao<T extends UserAmountTotal> extends BaseMapper<T> {
    void batchIncrease(@Param("list") Collection<UserAmountTotal> list);
}
