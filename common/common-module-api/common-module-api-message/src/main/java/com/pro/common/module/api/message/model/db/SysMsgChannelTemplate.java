package com.pro.common.module.api.message.model.db;

import com.pro.common.module.api.common.model.db.Country;
import com.pro.common.module.api.message.enums.EnumSysMsgBusinessCode;
import com.pro.common.module.api.message.enums.EnumSysMsgChannel;
import com.pro.common.module.api.message.enums.EnumSysMsgChannelType;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.model.classes.BaseConfigModel;
import com.pro.common.modules.api.dependencies.model.classes.IConfigClass;
import com.pro.framework.api.enums.IEnumToDbDb;
import com.pro.framework.api.enums.IEnumToDbDbId;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息(短信,邮件)模板
 * code 与 {@link EnumSysMsgBusinessCode} 对应
 *
 * @author admin
 */
@Data

@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "消息(短信,邮件)模板")
@JTDTable(entityId = 332)
public class SysMsgChannelTemplate extends BaseConfigModel implements IConfigClass, IEnumToDbDbId {
    @ApiModelProperty(value = "语言")
    @JTDField(defaultValue = CommonConst.Str.DEFAULT_LANG_EN, entityClass = Country.class, entityClassKey = "langCode")
    private String lang;
    @ApiModelProperty(value = "编号")
    private String businessCode;
    //经常有各自的appid,不同的消息走不同的appid或者模板要求,很难完全统一,所以最好区分通道
    @ApiModelProperty(value = "通道类型")
    private EnumSysMsgChannelType channelType;
    @ApiModelProperty(value = "三方通道")
    private EnumSysMsgChannel channel;
    @ApiModelProperty(value = "三方appId")
    private String appId;
    @ApiModelProperty(value = "内容模板")
    private String contentTemplate;
    @ApiModelProperty(value = "标题模板")
    private String titleTemplate;

    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "启用")//状态：1启用，0关闭
    private Boolean enabled;

    @ApiModelProperty(value = "内容模板是否富文本")
    private Boolean contentTemplateRichTextFlag;
    @ApiModelProperty(value = "三方审核通过的模板Id")
    private String outTemplateId;
    @ApiModelProperty(value = "自动触发消息业务事件")
    private String autoSendBusinessEvent;
    @ApiModelProperty(value = "特定位置文章可引用参数") // List<PosterEntity> 的json串
    private String paramEntitys;
    @ApiModelProperty(value = "弹窗文章编号")
    private String dialogPosterCode;
    @ApiModelProperty(value = "排序")
    private Integer sort;


    @ApiModelProperty(value = "备注")
    transient private String content;
    @ApiModelProperty(value = "标题")
    transient private String title;
}
