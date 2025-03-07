package com.pro.common.module.api.common.model.db;

import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.model.classes.BaseConfigModel;
import com.pro.common.modules.api.dependencies.model.classes.IOpenConfigClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "导航栏")
@JTDTable(entityId = 307)
public class Banner extends BaseConfigModel implements IOpenConfigClass {
    @Schema(description = "类型")
    @JTDField(entityClass = BannerType.class)
    private String type;
    @Schema(description = "语言")
    @JTDField(defaultValue = CommonConst.Str.DEFAULT_LANG_EN, entityClass = Country.class, entityClassKey = "langCode")
    private String lang;
    @Schema(description = "标题")
    private String title;
    @Schema(description = "中文名称")
    private String cname;
    @Schema(description = "简介")
    @JTDField(mainLength = 1000, uiType = JTDConst.EnumFieldUiType.textarea)
    private String intro;
    @Schema(description = "图片")
    @JTDField(uiType = JTDConst.EnumFieldUiType.image)
    private String pic;
    @Schema(description = "图片2")
    @JTDField(uiType = JTDConst.EnumFieldUiType.image, description = "选中图或封面图")
    private String picSelect;
    @Schema(description = "字体图标名称")
    private String icon;
    @Schema(description = "链接地址")
    private String url;
    @Schema(description = "样式")
    @JTDField(mainLength = 1000, uiType = JTDConst.EnumFieldUiType.textarea)
    private String style;
    @Schema(description = "是否自动播放")
    private Boolean autoPlay;
}
