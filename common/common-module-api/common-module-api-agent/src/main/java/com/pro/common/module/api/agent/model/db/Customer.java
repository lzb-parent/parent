package com.pro.common.module.api.agent.model.db;

import com.pro.common.module.api.agent.enums.EnumCustomerType;
import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.classes.IOpenConfigClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 客服
 *
 * @author admin
 */
@Data

@EqualsAndHashCode(callSuper = true)
@Schema(description = "客服")
@JTDTable(entityId = 311)
public class Customer extends BaseModel implements IOpenConfigClass {

    @Schema(description = "客服类型")
    private EnumCustomerType type;
    @Schema(description = "头像")
    @JTDField(uiType = JTDConst.EnumFieldUiType.image)
    private String headPic;

    @Schema(description = "代理id")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null, entityClass = Agent.class, entityClassKey = "id", entityClassLabel = "username")
    private String agentId;
    @Schema(description = "客服姓名")
    private String username;
    @Schema(description = "客服备注")
    private String subtitle;
    @Schema(description = "描述文章")
    @JTDField(type = JTDConst.EnumFieldType.text, uiType = JTDConst.EnumFieldUiType.richText)
    private String description;
    //    @Schema(description = "描述")
//    @JTDField(type = JTDConst.EnumFieldType.text, uiType = JTDConst.EnumFieldUiType.richText)
//    private String description;
    @Schema(description = "联系号码")
    private String number;
    @Schema(description = "链接地址")
    private String url;
    //    @Schema(description = "客户链接地址展示二维码")
//    private Boolean urlQr;
    @Schema(description = "工作开始时间")
    private String beginTime;
    @Schema(description = "工作结束时间")
    private String endTime;
    @Schema(description = "启用")
    private Boolean enabled;

    @Override
    public Integer getSort() {
        return 0;
    }
}
