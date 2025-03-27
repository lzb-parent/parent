package com.pro.common.modules.api.dependencies.enums;

import com.pro.framework.api.FrameworkConst;
import com.pro.framework.api.enums.EnumToDbEnum;
import com.pro.framework.api.enums.IEnumToDbEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 路由配置 基础
 */
@Getter
@AllArgsConstructor
@EnumToDbEnum(entityClass = "com.pro.common.module.api.common.model.db.Poster")
public enum EnumPoster implements IEnumToDbEnum {
    en_withdraw_user_close("en-US", "withdraw_user_close", "withdraw_user_close", null, "withdraw_user_close", "提现用户提现开关关闭", true),
    en_register_phone_error("en-US", "register_phone_error", "register_phone_error", null, "register_phone_error", "注册手机号格式不正确 {{phone}}:手机号(账号)", true),
    invite_info("en-US", "invite_info", "invite_info", null, "invite_info", "分享邀请页面", true),
    notice("en-US", "notice", "", null, "Welcome", "首页弹窗通知", true),
    ;
    private String lang;
    private String code;
    private String title;
    private String subtitle;
    private String content;
    private String remark;
    private Boolean enabled;
}
