package com.pro.common.module.service.pay.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pro.common.module.api.pay.model.db.PayBank;

/**
 * 例如 支付宝 微信 银行卡 Gcash PIX Dao
 */
public interface PayBankDao extends BaseMapper<PayBank> {

}
