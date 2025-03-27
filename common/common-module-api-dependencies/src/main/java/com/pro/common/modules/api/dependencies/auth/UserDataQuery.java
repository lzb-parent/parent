package com.pro.common.modules.api.dependencies.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户查询类")
public class UserDataQuery {
    @ApiModelProperty(value = "代理id")
    private Long agentId;
    @ApiModelProperty(value = "用户Id")
    private String userId;
    @ApiModelProperty(value = "是否统计所有下线")
    private Boolean isChildAll;
    @ApiModelProperty(value = "是否查询用户下级的信息")
    private Boolean userTeamFlag;
    @ApiModelProperty(value = "内部")
    private Boolean isDemo;
}
