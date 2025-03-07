package com.pro.common.module.api.pay.model.db;

import com.pro.common.module.api.pay.enums.EnumWithdrawStatus;
import com.pro.common.modules.api.dependencies.model.BaseUserModel;
import com.pro.common.modules.api.dependencies.model.classes.IUserOrderClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "单号")
    @JTDField(group = "基础信息")
    private String no;
    @Schema(description = "余额类型")//基础,佣金
    @JTDField(group = "基础信息")
    private String type;

    @Schema(description = "申请提款金额")//平台币
    @JTDField(group = "基础信息")
    private BigDecimal applyAmount;
    @Schema(description = "手续费")
    @JTDField(group = "基础信息")
    private BigDecimal fee;
    @Schema(description = "到账金额")
    @JTDField(group = "基础信息")
    private BigDecimal amount;
    @Schema(description = "提现状态")
    @JTDField(group = "基础信息")
    private EnumWithdrawStatus status;
    @Schema(description = "代理审核状态")
    @JTDField(group = "基础信息", notNull = JTDConst.EnumFieldNullType.can_null, defaultValue = "NULL")
    private EnumWithdrawStatus agentStatus;

    @Schema(description = "银行卡ID")
    @JTDField(group = "银行卡信息", notNull = JTDConst.EnumFieldNullType.can_null)
    private Long bankCardId;
    @Schema(description = "卡号类型编号")
    @JTDField(group = "银行卡信息", defaultValue = "BANK", entityClass = PayCardType.class)
    private String cardType;
    //    @Schema(description = "卡号类型名称")
//    @JTDField(group = "银行卡信息", defaultValue = "BANK", entityClass = PayCardType.class, entityClassTargetProp = "name")
//    private String cardTypeName;
    @Schema(description = "银行名称")
    @JTDField(group = "银行卡信息")
    private String bankName;
    @Schema(description = "分行代码")
    @JTDField(group = "银行卡信息")
    private String bankBranchCode;
    @Schema(description = "分行名称")
    @JTDField(group = "银行卡信息")
    private String bankBranchName;
    @Schema(description = "分行地址")
    @JTDField(group = "银行卡信息")
    private String bankAddress;
    @Schema(description = "开户人姓名")
    @JTDField(group = "银行卡信息")
    private String bankUsername;
    @Schema(description = "开户人手机")
    @JTDField(group = "银行卡信息")
    private String bankPhone;
    @Schema(description = "开户人地址")
    @JTDField(group = "银行卡信息")
    private String userAddress;
    @Schema(description = "开户人邮箱")
    @JTDField(group = "银行卡信息")
    private String email;
    @Schema(description = "开户人身份证号")
    @JTDField(group = "银行卡信息")
    private String idCard;
    @Schema(description = "收款二维码")
    @JTDField(group = "银行卡信息")
    private String pic;
    @Schema(description = "卡号账号")
    @JTDField(group = "银行卡信息")
    private String bankAccount;
    @Schema(description = "卡号二级", example = "例如_pix号码和cfp号码_才能定位唯一账号")
    @JTDField(group = "银行卡信息")
    private String bankAccount1;
    @Schema(description = "卡号三级", example = "")
    @JTDField(group = "银行卡信息")
    private String bankAccount2;

//    @Schema(description = "代付渠道code")
//    private String channelCode;
//    @Schema(description = "代付渠道")
//    private String channelType;

//    private BigDecimal balance; // 余额

    @Schema(description = "账变前余额")
    @JTDField(group = "结果信息")
    private BigDecimal beforeMoney;
    @Schema(description = "账变金额")
    @JTDField(group = "结果信息")
    private BigDecimal money;
    @Schema(description = "账变后余额")
    @JTDField(group = "结果信息")
    private BigDecimal afterMoney;
    @JTDField(group = "结果信息", mainLength = 2048, notNull = JTDConst.EnumFieldNullType.can_null)
    @Schema(description = "驳回原因")
    private String rejectReason; // 驳回理由
    @Schema(description = "货币符号")
    @JTDField(group = "结果信息")
    private String coinCode;
    @Schema(description = "货币汇率")
    @JTDField(group = "结果信息", defaultValue = "1")
    private BigDecimal exchangeRate;
    @Schema(description = "到账货币数额")// 扣除手续费 汇率换算
    @JTDField(group = "结果信息")
    private BigDecimal coinAmount;


//    private Boolean isAutoPayout; // 是否自动代付
//    private String autoPayoutResult; // 自动代付结果


//    @Schema(description = "第几单")
//    private Integer times;

//    @Schema(description = "是否首单")
//    private Boolean isFirst;
//    @Schema(description = "等级名称")
//    private String levelName;


    @Schema(description = "提款密码")
    transient private String tkPassword;
}
