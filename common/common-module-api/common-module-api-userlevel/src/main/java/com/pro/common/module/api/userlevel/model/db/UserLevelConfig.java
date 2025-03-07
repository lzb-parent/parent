package com.pro.common.module.api.userlevel.model.db;

import com.pro.common.modules.api.dependencies.model.classes.IOpenConfigClass;
import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.framework.api.enums.IEnumToDbDbId;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 用户等级
 *
 * @author admin
 */
@Data

@EqualsAndHashCode(callSuper = true)
//@Schema(description="用户等级配置")
//@JTDTable(
//        sequences = {
//                "UNIQUE KEY `uk_level` (`level`)",
//        }
//)
// 只需要生成 UserLevel 具体业务表
public class UserLevelConfig extends BaseModel implements IOpenConfigClass, IEnumToDbDbId {

    @Schema(description = "图片图标")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null, uiType = JTDConst.EnumFieldUiType.image, group = "基础信息")
    private String icon;
    @Schema(description = "字体图标")
    @JTDField(group = "基础信息")
    private String iconFront;
    @Schema(description = "等级名称")
    @JTDField(group = "基础信息")
    private String name;
    @Schema(description = "等级顺序值")
    @JTDField(group = "基础信息")
    private Integer level;
    @Schema(description = "描述文章")
    @JTDField(group = "基础信息")
    private String description;
    @Schema(description = "有效天数")
    @JTDField(group = "基础信息")
    private Integer validDays;
    @Schema(description = "排序")
    @JTDField(group = "基础信息")
    private Integer sort;
    @Schema(description = "启用")
    @JTDField(group = "基础信息")
    private Boolean enabled;
    @Schema(description = "享受推广佣金")
    @JTDField(group = "基础信息")
    private Boolean isEnjoyTgCommission;
    @Schema(description = "显示")
    @JTDField(group = "基础信息")
    private Boolean isOpen;
    @Schema(description = "备注")
    @JTDField(group = "基础信息")
    private String remark;
    @Schema(description = "佣金等级限制开关")
    @JTDField(group = "佣金信息")
    private Boolean commissionLevelLimitOpen;
    @Schema(description = "充值推广佣金百分比")
    @JTDField(group = "佣金信息",description = "可逗号分隔多级分润_例如配置三个数字就分三个人_单位百分比10_20_30")
    private String rechargeCommissionRate;

    @Schema(description = "升级购买价格")
    @JTDField(group = "升级配置")
    private BigDecimal upgradePrice;
    @Schema(description = "购买等级佣金百分比")
    private String buyVipCommissionRate;

//    @Schema(description = "充值最低金额单次")
//    @JTDField(group = "升级配置")
//    private BigDecimal rechargeMoneyMin;
//    @Schema(description = "升级最低余额限制")
//    @JTDField(group = "升级配置")
//    private BigDecimal balanceMin;
//    @Schema(description = "升级最高余额限制")
//    @JTDField(group = "升级配置")
//    private BigDecimal balanceMax;
//    @Schema(description = "升到该级奖励")
//    @JTDField(group = "升级配置")
//    private BigDecimal upgradeAward;



//    @Schema(description = "大转盘开关，默认开启，关闭后等级会员无法使用大转盘功能")
//    private Boolean isWheelOpen;
//    @Schema(description = "每隔多少天赠送1次转盘抽奖次数")
//    private Integer freeDrawDays;


//    @Schema(description = "佣金提现最低金额单次")
//    private BigDecimal balanceWithdrawMoneyMin;
//    @Schema(description = "佣金提现最高金额单次")
//    private BigDecimal balanceWithdrawMoneyMax;


//    @Schema(description = "个人累计在线充值")
//    private BigDecimal upgradeOnlineRechargeMoney;
//    @Schema(description = "自动升级个人余额最低限制")
//    private BigDecimal upgradeBalanceMin;
//    @Schema(description = "推广有效充值直接下级人数")
//    private Integer upgradeChildNumber;
//    @Schema(description = "直推下级个人累计在线充值")
//    private BigDecimal upgradeChildOnlineRechargeMoney;
//    @Schema(description = "直推下级个人首次在线充值金额")
//    private BigDecimal upgradeChildFirstOnlineRechargeMoney;
//    @Schema(description = "直推下级个人等级值，-1代表不限制")
//    @JTDField(defaultValue = "-1")
//    private Integer upgradeChildLevel;



//    @Schema(description = "赠送每日抽奖次数完成累计充值金额")//英文逗号分隔,从小到大
//    private String userWheelTimesPresentRechargeApplyMoneys;
//    @Schema(description = "赠送每日抽奖次数完成累计充值金额对应次数")//英文逗号分隔,从小到大
//    private String userWheelTimesPresentRechargeTimes;


//    @Schema(description = "提现次数/天")
//    @JTDField(defaultValue = "-1")
//    private Integer withdrawTimes;
//    @Schema(description = "免费提款次数/天")
//    @JTDField(defaultValue = "-1")
//    private Integer withdrawFreeTimes;
//    @Schema(description = "免费提款次数/月")
//    @JTDField(defaultValue = "-1")
//    private Integer withdrawFreeMonthTimes;
//    @Schema(description = "总免费提款次数")
//    @JTDField(defaultValue = "-1")
//    private Integer withdrawFreeTotalTimes;
//    @Schema(description = "超次后提现手续费")
//    private BigDecimal withdrawFee;
//    @Schema(description = "超次后提现固定手续费")
//    private BigDecimal withdrawFeeMoney;
//    @Schema(description = "提现最低金额单次")
//    private BigDecimal withdrawMoneyMin;
//    @Schema(description = "提现最高金额单次")
//    private BigDecimal withdrawMoneyMax;
//    @Schema(description = "本金提款注册天数限制") // 注册满多少天才能提款在线充值本金
//    private Integer withdrawRegisterDays;
//    @Schema(description = "本金提款推广人数限制") // 直推人数达到多少人才能提款在线充值本金
//    private Integer withdrawInviteNumber;
//    @Schema(description = "每日提款比例")
//    private BigDecimal withdrawDayRate;
//    @Schema(description = "提现金额不满足的文章")
//    @JTDField(defaultValue = "withdrawMoneyPoster")
//    private String withdrawMoneyPoster;
//    @Schema(description = "提现次数不满足的文章")
//    @JTDField(defaultValue = "withdrawTimesPoster")
//    private String withdrawTimesPoster;
}
