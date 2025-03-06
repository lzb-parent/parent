package com.pro.common.module.api.agent.enums;

import com.pro.common.module.api.agent.model.db.Agent;
import com.pro.framework.api.enums.IEnumToDbEnum;
import com.pro.framework.javatodb.annotation.JTDField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 路由配置 基础
 */
@Getter
@AllArgsConstructor
public enum EnumAgent implements IEnumToDbEnum<Agent> {
    _1(1L,
            "aaaaaa"
            , "AAC1CB3291207102C7B06445D957A5F3"
            , "333333"
            , 1
            , "2001" //             , EnumAuthRole._2001.getName().substring(1)
            , null
    ),

    ;
    private final Long id;
    private final String username;
    private final String password;
    private final String codeNumber;
    private final Integer level;
    private String roleIds;
    /**
     * 统一重置修改掉,这个配置时间以前的,旧的配置
     */
    private final String forceChangeTime;
}
