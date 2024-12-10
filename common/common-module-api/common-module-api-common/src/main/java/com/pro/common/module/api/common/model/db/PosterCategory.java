package com.pro.common.module.api.common.model.db;

import com.pro.common.modules.api.dependencies.model.classes.BaseConfigModel;
import com.pro.common.modules.api.dependencies.model.classes.IConfigClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文章分类表
 *
 * @author admin
 */
@Data

@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PosterCategory对象", description = "文章分类表")
@JTDTable(entityId = 325)
public class PosterCategory extends BaseConfigModel implements IConfigClass {
    @ApiModelProperty(value = "分类名称")
    private String name;
    @ApiModelProperty(value = "分类介绍")
    private String intro;
    @ApiModelProperty(value = "分类图标")
    @JTDField(uiType = JTDConst.EnumFieldUiType.image)
    private String icon;
}
