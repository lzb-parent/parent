package com.pro.common.module.api.pay.model.db;

import com.pro.common.module.api.pay.enums.EnumWithdrawStatus;
import com.pro.common.modules.api.dependencies.model.BaseUserModel;
import com.pro.common.modules.api.dependencies.model.classes.IUserOrderClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 提卡记录
 */
@Data
@JTDTable(
        sequences = {
                "UNIQUE KEY `uk_no` (`no`)",
                "KEY `uk_userId` (`user_id`)"
        }, entityId = 353
)
public class UserWithdraw extends BaseUserModel implements IUserOrderClass {
    @ApiModelProperty("单号")
    @JTDField(group = "基础信息")
    private String no;
    @ApiModelProperty(value = "余额类型")//基础,佣金
    @JTDField(group = "基础信息")
    private String type;

    @ApiModelProperty("申请提款金额")//平台币
    @JTDField(group = "基础信息")
    private BigDecimal applyAmount;
    @ApiModelProperty("手续费")
    @JTDField(group = "基础信息")
    private BigDecimal fee;
    @ApiModelProperty("到账金额")
    @JTDField(group = "基础信息")
    private BigDecimal amount;
    @ApiModelProperty("提现状态")
    @JTDField(group = "基础信息")
    private EnumWithdrawStatus status;
    @ApiModelProperty(value = "代理审核状态")
    @JTDField(group = "基础信息", notNull = JTDConst.EnumFieldNullType.can_null, defaultValue = "NULL")
    private EnumWithdrawStatus agentStatus;

    @ApiModelProperty("银行卡ID")
    @JTDField(group = "银行卡信息", notNull = JTDConst.EnumFieldNullType.can_null)
    private Long bankCardId;
    @ApiModelProperty("卡号类型编号")
    @JTDField(group = "银行卡信息", defaultValue = "BANK", entityClass = PayCardType.class)
    private String cardType;
    //    @ApiModelProperty("卡号类型名称")
//    @JTDField(group = "银行卡信息", defaultValue = "BANK", entityClass = PayCardType.class, entityClassTargetProp = "name")
//    private String cardTypeName;
    @ApiModelProperty("银行名称")
    @JTDField(group = "银行卡信息")
    private String bankName;
    @ApiModelProperty("分行代码")
    @JTDField(group = "银行卡信息")
    private String bankBranchCode;
    @ApiModelProperty("分行名称")
    @JTDField(group = "银行卡信息")
    private String bankBranchName;
    @ApiModelProperty("分行地址")
    @JTDField(group = "银行卡信息")
    private String bankAddress;
    @ApiModelProperty("开户人姓名")
    @JTDField(group = "银行卡信息")
    private String bankUsername;
    @ApiModelProperty("开户人手机")
    @JTDField(group = "银行卡信息")
    private String bankPhone;
    @ApiModelProperty("开户人地址")
    @JTDField(group = "银行卡信息")
    private String userAddress;
    @ApiModelProperty(value = "开户人邮箱")
    @JTDField(group = "银行卡信息")
    private String email;
    @ApiModelProperty(value = "开户人身份证号")
    @JTDField(group = "银行卡信息")
    private String idCard;
    @ApiModelProperty(value = "收款二维码")
    @JTDField(group = "银行卡信息")
    private String pic;
    @ApiModelProperty("卡号账号")
    @JTDField(group = "银行卡信息")
    private String bankAccount;
    @ApiModelProperty(value = "卡号二级", notes = "例如_pix号码和cfp号码_才能定位唯一账号")
    @JTDField(group = "银行卡信息")
    private String bankAccount1;
    @ApiModelProperty(value = "卡号三级", notes = "")
    @JTDField(group = "银行卡信息")
    private String bankAccount2;

//    @ApiModelProperty(value = "代付渠道code")
//    private String channelCode;
//    @ApiModelProperty(value = "代付渠道")
//    private String channelType;

//    private BigDecimal balance; // 余额

    @ApiModelProperty(value = "账变前余额")
    @JTDField(group = "结果信息")
    private BigDecimal beforeMoney;
    @ApiModelProperty(value = "账变金额")
    @JTDField(group = "结果信息")
    private BigDecimal money;
    @ApiModelProperty(value = "账变后余额")
    @JTDField(group = "结果信息")
    private BigDecimal afterMoney;
    @JTDField(group = "结果信息", mainLength = 2048, notNull = JTDConst.EnumFieldNullType.can_null)
    @ApiModelProperty(value = "驳回原因")
    private String rejectReason; // 驳回理由
    @ApiModelProperty(value = "货币符号")
    @JTDField(group = "结果信息")
    private String coinCode;
    @ApiModelProperty(value = "货币汇率")
    @JTDField(group = "结果信息", defaultValue = "1")
    private BigDecimal exchangeRate;
    @ApiModelProperty("到账货币数额")// 扣除手续费 汇率换算
    @JTDField(group = "结果信息")
    private BigDecimal coinAmount;


//    private Boolean isAutoPayout; // 是否自动代付
//    private String autoPayoutResult; // 自动代付结果


//    @ApiModelProperty(value = "第几单")
//    private Integer times;

//    @ApiModelProperty(value = "是否首单")
//    private Boolean isFirst;
//    @ApiModelProperty(value = "等级名称")
//    private String levelName;


    @ApiModelProperty(value = "提款密码")
    transient private String tkPassword;
}
