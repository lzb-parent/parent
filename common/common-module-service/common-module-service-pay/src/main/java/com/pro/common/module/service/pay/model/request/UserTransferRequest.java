package com.pro.common.module.service.pay.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserTransferRequest {

  @Schema(description = "用户id")
  private Long userId;
  @Schema(description = "会员账号")
  private String username;
  @Schema(description = "转账单号")
  private String no;
  @Schema(description = "转账方式，如UPI代付")
  private String payType;
  @Schema(description = "商户编号")
  private String merchantCode;
  @Schema(description = "商户单号")
  private String payNo;
  @Schema(description = "银行id（目前UPI代付使用）")
  private String bankId;
  @Schema(description = "转账前金额")
  private BigDecimal beforeMoney;
  @Schema(description = "转账金额")
  private BigDecimal money;
  @Schema(description = "转账后金额")
  private BigDecimal afterMoney;
  @Schema(description = "转账状态")
  private String state;
  @Schema(description = "是否内部用户")
  private Boolean isDemo;

  @Schema(description = "账户类型，默认银行卡：BANK")
  private String cardType;
  @Schema(description = "银行名称")
  private String bankName;
  @Schema(description = "银行地址")
  private String bankAddress;
  @Schema(description = "银行卡号")
  private String bankAccount;
  @Schema(description = "开户人")
  private String bankUsername;
  @Schema(description = "预留手机号")
  private String bankPhone;
  @Schema(description = "电子邮箱")
  private String email;
  @Schema(description = "分行代码")
  private String bankBranchCode;
  @Schema(description = "分行名称")
  private String bankBranchName;
  @Schema(description = "收款人地址")
  private String userAddress;
  @Schema(description = "身份证号")
  private String idCard;

//  @Schema(description = "印度IFSC编码")
//  private String ifsc;
//  @Schema(description = "印度UPI编码")
//  private String upi;
//  @Schema(description = "巴西PIX账号")
//  private String pix;
//  @Schema(description = "巴西CPF编码")
//  private String cpf;
//  @Schema(description = "SWIFT")
//  private String swift;
//  @Schema(description = "菲律宾GCASH")
//  private String gcash;
//  @Schema(description = "哥伦比亚POWWI")
//  private String powwi;
//  @Schema(description = "CCI账号")
//  private String cci;
//  @Schema(description = "哥伦比亚NEQUI")
//  private String nequi;
//  @Schema(description = "土耳其iban")
//  private String iban;
  @Schema(description = "USDT")
  private String usdt;

  @Schema(description = "钱包地址，用于后期其他国家钱包通用字段")
  private String wallet;
  @Schema(description = "二级卡号", example = "例如_pix号码和cfp号码_才能定位唯一账号")
  private String bankAccount1;
  @Schema(description = "三级卡号", example = "")
  private String bankAccount2;

  @Schema(description = "代付渠道编码")
  private String channelCode;
  @Schema(description = "代付渠道币种")
  private String channelCoinCode;


}
