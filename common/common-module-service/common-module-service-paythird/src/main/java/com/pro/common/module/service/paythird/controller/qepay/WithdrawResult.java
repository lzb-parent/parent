package com.pro.common.module.service.paythird.controller.qepay;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawResult {

    private Integer tradeResult;
    private String merNo;
    private String tradeNo;
    private String merTransferId;
    private String applyDate;
    private String version;
    private String respCode;
    private BigDecimal transferAmount;
    private String sign;

}
