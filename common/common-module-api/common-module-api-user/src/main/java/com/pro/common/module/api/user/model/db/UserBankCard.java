package com.pro.common.module.api.user.model.db;

import com.pro.common.module.api.pay.model.db.PayCardType;
import com.pro.common.modules.api.dependencies.model.BaseUserModel;
import com.pro.common.modules.api.dependencies.model.classes.IUserDataClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 会员银行卡
 */
@Data
@Schema(description = "用户的提现账号")
@JTDTable(entityId = 12, module = "user")
public class UserBankCard extends BaseUserModel implements IUserDataClass {
    @Schema(description = "账户类型")
    @JTDField(defaultValue = "BANK", entityClass = PayCardType.class)
    private String cardType;
    @Schema(description = "银行名称")
    @JTDField(sortable = true)
    private String bankName;
    @Schema(description = "分行代码")
    private String bankBranchCode;
    @Schema(description = "分行名称")
    private String bankBranchName;
    @Schema(description = "分行地址")
    private String bankAddress;
    @Schema(description = "姓名")
    private String bankUsername;
    @Schema(description = "手机")
    private String bankPhone;
    @Schema(description = "地址")
    private String userAddress;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "身份证号")
    private String idCard;
    @Schema(description = "收款二维码")
    @JTDField(uiType = JTDConst.EnumFieldUiType.image)
    private String pic;
    //    @Schema(description = "USDT地址")
//    private String usdt;
    @Schema(description = "账号")
    private String bankAccount;
    @Schema(description = "二级账号", example = "例如_pix号码和cfp号码_才能定位唯一账号")
    private String bankAccount1;
    @Schema(description = "三级账号")
    private String bankAccount2;
    @Schema(description = "排序")
    @JTDField(defaultValue = "100")
    private Integer sort;

    // 提款密码
    transient private String tkPassword;

}
