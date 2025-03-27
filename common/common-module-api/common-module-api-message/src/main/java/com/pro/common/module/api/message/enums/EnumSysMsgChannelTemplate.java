package com.pro.common.module.api.message.enums;

import com.pro.common.module.api.common.model.db.Country;
import com.pro.common.module.api.message.model.db.SysMsgChannelMerchant;
import com.pro.common.module.api.message.model.db.SysMsgChannelTemplate;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.framework.api.enums.IEnumToDbEnum;
import com.pro.framework.javatodb.annotation.JTDField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.pro.common.module.api.message.enums.EnumSysMsgChannel.*;
import static com.pro.common.module.api.message.enums.EnumSysMsgChannelType.*;

/**
 * 路由配置 基础
 * userMoneyWait
 * userMoneyWaitRecord
 * posterCode
 * authRoleRoute
 * sysMultiClassRelation
 * 暂时不做路由
 */
@Getter
@AllArgsConstructor
public enum EnumSysMsgChannelTemplate implements IEnumToDbEnum<SysMsgChannelTemplate> {
    _1(CommonConst.Str.DEFAULT_LANG_EN, EnumSysMsgBusinessCode.REGISTER_CODE.name(), EMAIL, EMAIL_GOOGLE, null, "Your verification code is {code}.", null, null, null, null, null, null, null, null, null, null),
    _2(CommonConst.Str.DEFAULT_LANG_EN, EnumSysMsgBusinessCode.FORGET_PWD_CODE.name(), EMAIL, EMAIL_GOOGLE, null, "Your verification code is {code}.", null, null, null, null, null, null, null, null, null, null),
    _3(CommonConst.Str.DEFAULT_LANG_EN, EnumSysMsgBusinessCode.FORGET_TK_PWD_CODE.name(), EMAIL, EMAIL_GOOGLE, null, "Your verification code is {code}.", null, null, null, null, null, null, null, null, null, null),
    ;
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

    /**
     * 统一重置修改掉,这个配置时间以前的,旧的配置
     */
    private final String forceChangeTime;
}
