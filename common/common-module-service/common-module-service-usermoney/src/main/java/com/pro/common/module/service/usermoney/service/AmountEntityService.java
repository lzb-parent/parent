package com.pro.common.module.service.usermoney.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pro.common.module.service.usermoney.dao.AmountEntityDao;
import com.pro.common.module.api.usermoney.model.modelbase.AmountEntity;

/**
 * 数额变化的基础类
 */
//@Service
public abstract class AmountEntityService extends ServiceImpl<AmountEntityDao, AmountEntity> implements IAmountEntityService<AmountEntity>{
}
