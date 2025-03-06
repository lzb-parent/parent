package com.pro.common.module.api.user.model.db;

import com.pro.common.module.api.pay.model.db.PayCardType;
import com.pro.common.modules.api.dependencies.model.BaseUserModel;
import com.pro.common.modules.api.dependencies.model.classes.IUserDataClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 会员银行卡
 */
@Data
@ApiModel(description = "用户的提现账号")
@JTDTable(entityId = 12, module = "user")
public class UserBankCard extends BaseUserModel implements IUserDataClass {
    @ApiModelProperty("账户类型")
    @JTDField(defaultValue = "BANK", entityClass = PayCardType.class)
    private String cardType;
    @ApiModelProperty("银行名称")
    @JTDField(sortable = true)
    private String bankName;
    @ApiModelProperty("分行代码")
    private String bankBranchCode;
    @ApiModelProperty("分行名称")
    private String bankBranchName;
    @ApiModelProperty("分行地址")
    private String bankAddress;
    @ApiModelProperty("姓名")
    private String bankUsername;
    @ApiModelProperty("手机")
    private String bankPhone;
    @ApiModelProperty("地址")
    private String userAddress;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "身份证号")
    private String idCard;
    @ApiModelProperty(value = "收款二维码")
    @JTDField(uiType = JTDConst.EnumFieldUiType.image)
    private String pic;
    //    @ApiModelProperty(value = "USDT地址")
//    private String usdt;
    @ApiModelProperty("账号")
    private String bankAccount;
    @ApiModelProperty(value = "二级账号", notes = "例如_pix号码和cfp号码_才能定位唯一账号")
    private String bankAccount1;
    @ApiModelProperty(value = "三级账号")
    private String bankAccount2;
    @ApiModelProperty(value = "排序")
    @JTDField(defaultValue = "100")
    private Integer sort;

    // 提款密码
    transient private String tkPassword;

}
