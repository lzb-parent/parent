package com.pro.common.module.api.usermoney.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pro.common.module.api.usermoney.model.modelbase.AmountEntity;
import org.springframework.stereotype.Service;

/**
 * 数额变化的基础类
 */
@Service
public interface IAmountEntityService<Entity extends AmountEntity> extends IService<Entity> {
}
