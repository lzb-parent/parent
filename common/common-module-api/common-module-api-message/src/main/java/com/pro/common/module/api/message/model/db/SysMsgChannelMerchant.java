package com.pro.common.module.api.message.model.db;

import com.pro.common.module.api.message.enums.EnumSysMsgBusinessCode;
import com.pro.common.module.api.message.enums.EnumSysMsgChannel;
import com.pro.common.module.api.message.enums.EnumSysMsgChannelType;
import com.pro.common.modules.api.dependencies.model.classes.BaseConfigModel;
import com.pro.common.modules.api.dependencies.model.classes.IAdminClass;
import com.pro.framework.api.enums.IEnumToDbDb;
import com.pro.framework.api.enums.IEnumToDbDbId;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 消息(短信,邮件)模板
 * code 与 {@link EnumSysMsgBusinessCode} 对应
 *
 * @author admin
 */
@Data

@EqualsAndHashCode(callSuper = true)
@Schema(description = "短信通道配置")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JTDTable(entityId = 331)
public class SysMsgChannelMerchant extends BaseConfigModel implements IAdminClass, IEnumToDbDbId {
    @Schema(description = "通道类型")
    private EnumSysMsgChannelType channelType;
    @Schema(description = "通道方")
    private EnumSysMsgChannel channel;
    @Schema(description = "接口入口地址")
    private String baseUrl;
    @Schema(description = "接口账号")
    private String apiKey;
    @Schema(description = "接口密码")
    private String apiPwd;
    @Schema(description = "接口应用_官网备注信息等")
    private String remark;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "启用")
    private Boolean enabled;
    @Schema(description = "其他配置")
    @JTDField(description = "比如邮箱端口协议等")
    private String otherJson;
}
