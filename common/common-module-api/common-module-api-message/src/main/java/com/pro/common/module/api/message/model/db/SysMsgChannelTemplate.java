package com.pro.common.module.api.message.model.db;

import com.pro.common.module.api.common.model.db.Country;
import com.pro.common.module.api.message.enums.EnumSysMsgBusinessCode;
import com.pro.common.module.api.message.enums.EnumSysMsgChannel;
import com.pro.common.module.api.message.enums.EnumSysMsgChannelType;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.model.classes.BaseConfigModel;
import com.pro.common.modules.api.dependencies.model.classes.IOpenConfigClass;
import com.pro.framework.api.enums.IEnumToDbDbId;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "消息(短信,邮件)模板")
@JTDTable(entityId = 332)
public class SysMsgChannelTemplate extends BaseConfigModel implements IOpenConfigClass, IEnumToDbDbId {
    @Schema(description = "语言")
    @JTDField(defaultValue = CommonConst.Str.DEFAULT_LANG_EN, entityClass = Country.class, entityClassKey = "langCode")
    private String lang;
    @Schema(description = "编号")
    private String businessCode;
    //经常有各自的appid,不同的消息走不同的appid或者模板要求,很难完全统一,所以最好区分通道
    @Schema(description = "通道类型")
    private EnumSysMsgChannelType channelType;
    @Schema(description = "三方通道")
    private EnumSysMsgChannel channel;
    @Schema(description = "三方appId")
    private String appId;
    @Schema(description = "内容模板")
    private String contentTemplate;
    @Schema(description = "标题模板")
    private String titleTemplate;

    @Schema(description = "备注")
    private String remark;
    @Schema(description = "启用")//状态：1启用，0关闭
    private Boolean enabled;

    @Schema(description = "内容模板是否富文本")
    private Boolean contentTemplateRichTextFlag;
    @Schema(description = "三方审核通过的模板Id")
    private String outTemplateId;
    @Schema(description = "自动触发消息业务事件")
    private String autoSendBusinessEvent;
    @Schema(description = "特定位置文章可引用参数") // List<PosterEntity> 的json串
    private String paramEntitys;
    @Schema(description = "弹窗文章编号")
    private String dialogPosterCode;
    @Schema(description = "排序")
    private Integer sort;


    @Schema(description = "备注")
    transient private String content;
    @Schema(description = "标题")
    transient private String title;
}
