package com.pro.common.module.api.usermoney.service;

import org.springframework.stereotype.Service;

/**
 * 数额变化的基础类
 */
@Service
public interface IDayClearService {
    void clearDay();
}
