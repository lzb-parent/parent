package com.pro.common.module.api.agent.model.db;

import com.pro.common.module.api.agent.enums.EnumCustomerType;
import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.classes.IConfigClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 客服
 *
 * @author admin
 */
@Data

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "客服")
@JTDTable(entityId = 311)
public class Customer extends BaseModel implements IConfigClass {

    @ApiModelProperty(value = "客服类型")
    private EnumCustomerType type;
    @ApiModelProperty(value = "头像")
    @JTDField(uiType = JTDConst.EnumFieldUiType.image)
    private String headPic;

    @ApiModelProperty(value = "代理id")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null, entityClass = Agent.class, entityClassKey = "id", entityClassLabel = "username")
    private String agentId;
    @ApiModelProperty(value = "客服姓名")
    private String username;
    @ApiModelProperty(value = "客服备注")
    private String subtitle;
    @ApiModelProperty(value = "描述文章")
    @JTDField(type = JTDConst.EnumFieldType.text, uiType = JTDConst.EnumFieldUiType.richText)
    private String description;
    //    @ApiModelProperty(value = "描述")
//    @JTDField(type = JTDConst.EnumFieldType.text, uiType = JTDConst.EnumFieldUiType.richText)
//    private String description;
    @ApiModelProperty(value = "联系号码")
    private String number;
    @ApiModelProperty(value = "链接地址")
    private String url;
    //    @ApiModelProperty(value = "客户链接地址展示二维码")
//    private Boolean urlQr;
    @ApiModelProperty(value = "工作开始时间")
    private String beginTime;
    @ApiModelProperty(value = "工作结束时间")
    private String endTime;
    @ApiModelProperty(value = "启用")
    private Boolean enabled;

    @Override
    public Integer getSort() {
        return 0;
    }
}
