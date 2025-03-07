package com.pro.common.module.api.message.model.db;

import com.pro.common.module.api.message.enums.EnumSysMsgChannel;
import com.pro.common.module.api.message.enums.EnumSysMsgChannelType;
import com.pro.common.module.api.message.enums.EnumSysMsgState;
import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.classes.IUserRecordClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "短信发送记录")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JTDTable(entityId = 333)
public class SysMsgRecord extends BaseModel implements IUserRecordClass {
    @Schema(description = "消息类型")
    private EnumSysMsgChannelType channelType;
    @Schema(description = "模板编号")
    private String businessCode;
    // private EnumSmsBusinessCode businessCode;
    @Schema(description = "短信编号")
    private String no;
    @Schema(description = "短信id")
    private String sid;
    @Schema(description = "账号")
    @JTDField(description = "手机号或邮箱")
    private String account;
    @Schema(description = "标题模板")

    private String titleTemplate;
    @Schema(description = "内容模板")
    @JTDField(type = JTDConst.EnumFieldType.text)
    private String contentTemplate;
    @Schema(description = "内容参数JSON")
    @JTDField(type = JTDConst.EnumFieldType.text, notNull = JTDConst.EnumFieldNullType.can_null)
    private String paramJson;
    // @Schema(description = "其他内容信息:邮箱标题,附件等")
    //
    // private String contentOtherJson;
    @Schema(description = "发送状态")//：已提交，已发送，发送失败，已使用，已过期
    private EnumSysMsgState state;
    @Schema(description = "发送状态描述")
    private String stateMessage;
    // @Schema(description = "过期时间")
    // private LocalDateTime overTime;
    @Schema(description = "发起人用户Id")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private Long userId;
    @Schema(description = "ip")
    private String ip;
    @Schema(description = "短信计费条数")
    private Integer smsCount;
    @Schema(description = "短信计费条数")
    private BigDecimal fee;
    @Schema(description = "计费单位")
    @JTDField(description = "例如：RMB")
    private String unit;

    @Schema(description = "用户名")
    private String fromUsername;
    @Schema(description = "发起人代理人Id")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private Long fromAgentId;
    @Schema(description = "发起人代理人账号")
    private String fromAgentUsername;
    @Schema(description = "消息通道")
    private EnumSysMsgChannel channel;
    @Schema(description = "弹窗文章编号")
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
