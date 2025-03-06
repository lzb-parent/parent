package com.pro.common.module.service.pay.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserTransferRequest {

  @ApiModelProperty(value = "用户id")
  private Long userId;
  @ApiModelProperty(value = "会员账号")
  private String username;
  @ApiModelProperty(value = "转账单号")
  private String no;
  @ApiModelProperty(value = "转账方式，如UPI代付")
  private String payType;
  @ApiModelProperty(value = "商户编号")
  private String merchantCode;
  @ApiModelProperty(value = "商户单号")
  private String payNo;
  @ApiModelProperty(value = "银行id（目前UPI代付使用）")
  private String bankId;
  @ApiModelProperty(value = "转账前金额")
  private BigDecimal beforeMoney;
  @ApiModelProperty(value = "转账金额")
  private BigDecimal money;
  @ApiModelProperty(value = "转账后金额")
  private BigDecimal afterMoney;
  @ApiModelProperty(value = "转账状态")
  private String state;
  @ApiModelProperty(value = "是否内部用户")
  private Boolean isDemo;

  @ApiModelProperty(value = "账户类型，默认银行卡：BANK")
  private String cardType;
  @ApiModelProperty(value = "银行名称")
  private String bankName;
  @ApiModelProperty(value = "银行地址")
  private String bankAddress;
  @ApiModelProperty(value = "银行卡号")
  private String bankAccount;
  @ApiModelProperty(value = "开户人")
  private String bankUsername;
  @ApiModelProperty(value = "预留手机号")
  private String bankPhone;
  @ApiModelProperty(value = "电子邮箱")
  private String email;
  @ApiModelProperty(value = "分行代码")
  private String bankBranchCode;
  @ApiModelProperty(value = "分行名称")
  private String bankBranchName;
  @ApiModelProperty(value = "收款人地址")
  private String userAddress;
  @ApiModelProperty(value = "身份证号")
  private String idCard;

//  @ApiModelProperty(value = "印度IFSC编码")
//  private String ifsc;
//  @ApiModelProperty(value = "印度UPI编码")
//  private String upi;
//  @ApiModelProperty(value = "巴西PIX账号")
//  private String pix;
//  @ApiModelProperty(value = "巴西CPF编码")
//  private String cpf;
//  @ApiModelProperty(value = "SWIFT")
//  private String swift;
//  @ApiModelProperty(value = "菲律宾GCASH")
//  private String gcash;
//  @ApiModelProperty(value = "哥伦比亚POWWI")
//  private String powwi;
//  @ApiModelProperty(value = "CCI账号")
//  private String cci;
//  @ApiModelProperty(value = "哥伦比亚NEQUI")
//  private String nequi;
//  @ApiModelProperty(value = "土耳其iban")
//  private String iban;
  @ApiModelProperty(value = "USDT")
  private String usdt;

  @ApiModelProperty(value = "钱包地址，用于后期其他国家钱包通用字段")
  private String wallet;
  @ApiModelProperty(value = "二级卡号", notes = "例如_pix号码和cfp号码_才能定位唯一账号")
  private String bankAccount1;
  @ApiModelProperty(value = "三级卡号", notes = "")
  private String bankAccount2;

  @ApiModelProperty(value = "代付渠道编码")
  private String channelCode;
  @ApiModelProperty(value = "代付渠道币种")
  private String channelCoinCode;


}
