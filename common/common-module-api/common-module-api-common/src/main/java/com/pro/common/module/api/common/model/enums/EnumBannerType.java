package com.pro.common.module.api.common.model.enums;

import com.pro.common.module.api.common.model.db.BannerType;
import com.pro.framework.api.enums.IEnum;
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
public enum EnumBannerType implements IEnumToDbEnum<BannerType> , IEnum {
    _1("home_carousel", "首页轮播", null, null, ""),
    _2("userCenter_menus", "个人中心_菜单列表", null, null, ""),
    _3("bottom", "整站底部导航", null, null, ""),
    _4("home_right", "首页右侧悬浮", null, null, ""),
    ;
    private String code;
    @Schema(description = "名称")
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

    @Override
    public String getLabel() {
        return name;
    }
}
