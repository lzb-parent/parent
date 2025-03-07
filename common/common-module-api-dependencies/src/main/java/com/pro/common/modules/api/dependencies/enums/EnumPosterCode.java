package com.pro.common.modules.api.dependencies.enums;

import com.pro.framework.api.enums.EnumToDbEnum;
import com.pro.framework.api.enums.IEnumToDbEnum;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 路由配置 基础
 */
@Getter
@AllArgsConstructor
@EnumToDbEnum(entityClass = "com.pro.common.module.api.common.model.db.PosterCode")
public enum EnumPosterCode implements IEnumToDbEnum {
    withdraw_user_close("提现用户提现开关关闭", null, null, ""),
    withdraw_user_fans_close("提现用户提现粉丝开关关闭", null, null, ""),
    register_phone_error("注册手机号格式不正确", null, null, ""),
    notice("首页弹窗通知", null, null, ""),
    ;
    //    @Schema(description = "固有文章编号")
//    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
//    private String code; // 等同于name
    @Schema(description = "中文名字")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String name;
    @Schema(description = "启用")
    private Boolean enabled;
    @Schema(description = "排序")
    @JTDField(defaultValue = "100")
    private Integer sort;
    @Schema(description = "内部描述")
    @JTDField(uiType = JTDConst.EnumFieldUiType.textarea)
    private String remark;
}
