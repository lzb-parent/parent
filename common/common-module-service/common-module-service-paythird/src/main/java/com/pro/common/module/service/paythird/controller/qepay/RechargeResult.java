package com.pro.common.module.service.paythird.controller.qepay;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RechargeResult {

    private Integer tradeResult;
    private String mchId;
    private String orderNo;
    private String mchOrderNo;
    private String orderDate;
    private BigDecimal amount;
    private BigDecimal oriAmount;
    private String sign;

}
