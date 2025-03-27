package com.pro.common.module.api.message.model.db;

import com.pro.common.module.api.message.enums.EnumSysMsgChannel;
import com.pro.common.module.api.message.enums.EnumSysMsgChannelType;
import com.pro.common.module.api.message.enums.EnumSysMsgState;
import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.classes.IUserRecordClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 短信发送记录
 *
 * @author admin
 */
@Data
//@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "短信发送记录")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JTDTable(entityId = 333)
public class SysMsgRecord extends BaseModel implements IUserRecordClass {
    @ApiModelProperty(value = "消息类型")
    private EnumSysMsgChannelType channelType;
    @ApiModelProperty(value = "模板编号")
    private String businessCode;
    // private EnumSmsBusinessCode businessCode;
    @ApiModelProperty(value = "短信编号")
    private String no;
    @ApiModelProperty(value = "短信id")
    private String sid;
    @ApiModelProperty(value = "账号")
    @JTDField(description = "手机号或邮箱")
    private String account;
    @ApiModelProperty(value = "标题模板")

    private String titleTemplate;
    @ApiModelProperty(value = "内容模板")
    @JTDField(type = JTDConst.EnumFieldType.text)
    private String contentTemplate;
    @ApiModelProperty(value = "内容参数JSON")
    @JTDField(type = JTDConst.EnumFieldType.text, notNull = JTDConst.EnumFieldNullType.can_null)
    private String paramJson;
    // @ApiModelProperty(value = "其他内容信息:邮箱标题,附件等")
    //
    // private String contentOtherJson;
    @ApiModelProperty(value = "发送状态")//：已提交，已发送，发送失败，已使用，已过期
    private EnumSysMsgState state;
    @ApiModelProperty(value = "发送状态描述")
    private String stateMessage;
    // @ApiModelProperty(value = "过期时间")
    // private LocalDateTime overTime;
    @ApiModelProperty(value = "发起人用户Id")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private Long userId;
    @ApiModelProperty(value = "ip")
    private String ip;
    @ApiModelProperty(value = "短信计费条数")
    private Integer smsCount;
    @ApiModelProperty(value = "短信计费条数")
    private BigDecimal fee;
    @ApiModelProperty(value = "计费单位")
    @JTDField(description = "例如：RMB")
    private String unit;

    @ApiModelProperty(value = "用户名")
    private String fromUsername;
    @ApiModelProperty(value = "发起人代理人Id")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private Long fromAgentId;
    @ApiModelProperty(value = "发起人代理人账号")
    private String fromAgentUsername;
    @ApiModelProperty(value = "消息通道")
    private EnumSysMsgChannel channel;
    @ApiModelProperty(value = "弹窗文章编号")
    private String dialogPosterCode;
    //    transient private SysMsgChannelMerchant merchant;
    transient private SysMsgChannelTemplate template;
    transient private Map<String, ?> paramMap;
//    transient private User user;

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
