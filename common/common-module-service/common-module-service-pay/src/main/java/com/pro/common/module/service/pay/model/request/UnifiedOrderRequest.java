package com.pro.common.module.service.pay.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "支付下单请求")
@Data
public class UnifiedOrderRequest {

  @Schema(description = "通道Id", hidden = true)
  private Long payChannelId;

  @Schema(description = "下单用户", hidden = true)
  private Long userId;

  @Schema(description = "商户编码")
  private String merchantCode;
  @Schema(description = "交易号")
  private String merchantNo;
//  @Schema(description = "方式")
//  private EnumRechargeType type = EnumRechargeType.RECHARGE_ONLINE;
  @Schema(description = "支付方式")
  private String payType;
  @Schema(description = "银行卡类型")//gcash maya bank ...
  private String cardType;
  @Schema(description = "同步回调地址")
  private String returnUrl;

  @Schema(description = "申请金额")
  private BigDecimal applyAmount;
  @Schema(description = "下单金额")
  private BigDecimal amount;

  @Schema(description = "付款账号")
  private String bankAccount;
  @Schema(description = "付款银行名称")
  private String bankName;
  @Schema(description = "付款人")
  private String bankUsername;
  @Schema(description = "真实姓名")
  private String realName;
  @Schema(description = "付款凭证")
  private String pic;

//  @Schema(description = "汇率：1平台币=*通道币")
//  private BigDecimal exchangeRate;
//  @Schema(description = "货币符号")
//  private String coinCode;

//  @Schema(description = "优惠券券码", hidden = true)
//  private String couponNo;

  // 用于其他业务直接充值付款场景
//  @Schema(description = "场景业务", hidden = true)
//  private String action;
//  @Schema(description = "场景业务关联id", hidden = true)
//  private String actionId;
//  @Schema(description = "场景业务关联id2", hidden = true)
//  private String actionId2;
}
