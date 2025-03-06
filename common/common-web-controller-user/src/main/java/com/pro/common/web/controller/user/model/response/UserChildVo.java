package com.pro.common.web.controller.user.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserChildVo {

    private Long levelId;
    private String levelName;
    private int levelValue;
    private Long pid;
    private String pusername;
    private Long userId;
    private String username;
    private int level;
    private BigDecimal commission;
    private BigDecimal totalRechargeMoney;
    private BigDecimal totalTkMoney;
    private LocalDateTime createTime;

    private List<UserChildVo> children;
}
