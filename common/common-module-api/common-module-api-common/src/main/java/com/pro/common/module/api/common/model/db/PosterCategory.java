package com.pro.common.module.api.common.model.db;

import com.pro.common.modules.api.dependencies.model.classes.BaseConfigModel;
import com.pro.common.modules.api.dependencies.model.classes.IOpenConfigClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文章分类表
 *
 * @author admin
 */
@Data

@EqualsAndHashCode(callSuper = true)
@Schema(description = "文章分类表")
@JTDTable(entityId = 325)
public class PosterCategory extends BaseConfigModel implements IOpenConfigClass {
    @Schema(description = "分类名称")
    private String name;
    @Schema(description = "分类介绍")
    private String intro;
    @Schema(description = "分类图标")
    @JTDField(uiType = JTDConst.EnumFieldUiType.image)
    private String icon;
}
