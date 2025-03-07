package com.pro.common.modules.api.dependencies.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户查询类")
public class UserDataQuery {
    @Schema(description = "代理id")
    private Long agentId;
    @Schema(description = "用户Id")
    private String userId;
    @Schema(description = "是否统计所有下线")
    private Boolean isChildAll;
    @Schema(description = "是否查询用户下级的信息")
    private Boolean userTeamFlag;
    @Schema(description = "内部")
    private Boolean isDemo;
}
