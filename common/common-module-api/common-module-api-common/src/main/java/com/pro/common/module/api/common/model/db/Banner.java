package com.pro.common.module.api.common.model.db;

import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.model.classes.BaseConfigModel;
import com.pro.common.modules.api.dependencies.model.classes.IConfigClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "导航栏")
@JTDTable(entityId = 307)
public class Banner extends BaseConfigModel implements IConfigClass {
    @ApiModelProperty(value = "类型")
    @JTDField(entityClass = BannerType.class)
    private String type;
    @ApiModelProperty(value = "语言")
    @JTDField(defaultValue = CommonConst.Str.DEFAULT_LANG_EN, entityClass = Country.class, entityClassKey = "langCode")
    private String lang;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "中文名称")
    private String cname;
    @ApiModelProperty(value = "简介")
    @JTDField(mainLength = 1000, uiType = JTDConst.EnumFieldUiType.textarea)
    private String intro;
    @ApiModelProperty(value = "图片")
    @JTDField(uiType = JTDConst.EnumFieldUiType.image)
    private String pic;
    @ApiModelProperty(value = "图片2")
    @JTDField(uiType = JTDConst.EnumFieldUiType.image, description = "选中图或封面图")
    private String picSelect;
    @ApiModelProperty(value = "字体图标名称")
    private String icon;
    @ApiModelProperty(value = "链接地址")
    private String url;
    @ApiModelProperty(value = "样式")
    @JTDField(mainLength = 1000, uiType = JTDConst.EnumFieldUiType.textarea)
    private String style;
    @ApiModelProperty(value = "是否自动播放")
    private Boolean autoPlay;
}
