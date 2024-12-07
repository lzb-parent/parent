package com.pro.common.module.service.admin.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel(description = "支付下单请求")
@Data
public class UnifiedOrderRequest {

  @ApiModelProperty(value = "通道Id", hidden = true)
  private Long payChannelId;

  @ApiModelProperty(value = "下单用户", hidden = true)
  private Long userId;

  @ApiModelProperty(value = "商户编码")
  private String merchantCode;
  @ApiModelProperty(value = "交易号")
  private String merchantNo;
//  @ApiModelProperty(value = "方式")
//  private EnumRechargeType type = EnumRechargeType.RECHARGE_ONLINE;
  @ApiModelProperty(value = "支付方式")
  private String payType;
  @ApiModelProperty(value = "银行卡类型")//gcash maya bank ...
  private String cardType;
  @ApiModelProperty(value = "同步回调地址")
  private String returnUrl;

  @ApiModelProperty(value = "申请金额")
  private BigDecimal applyAmount;
  @ApiModelProperty(value = "下单金额")
  private BigDecimal amount;

  @ApiModelProperty(value = "付款账号")
  private String bankAccount;
  @ApiModelProperty(value = "付款银行名称")
  private String bankName;
  @ApiModelProperty(value = "付款人")
  private String bankUsername;
  @ApiModelProperty(value = "真实姓名")
  private String realName;
  @ApiModelProperty(value = "付款凭证")
  private String pic;

//  @ApiModelProperty(value = "汇率：1平台币=*通道币")
//  private BigDecimal exchangeRate;
//  @ApiModelProperty(value = "货币符号")
//  private String coinCode;

//  @ApiModelProperty(value = "优惠券券码", hidden = true)
//  private String couponNo;

  // 用于其他业务直接充值付款场景
//  @ApiModelProperty(value = "场景业务", hidden = true)
//  private String action;
//  @ApiModelProperty(value = "场景业务关联id", hidden = true)
//  private String actionId;
//  @ApiModelProperty(value = "场景业务关联id2", hidden = true)
//  private String actionId2;
}
