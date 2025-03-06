package com.pro.common.web.controller.user.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TeamChildInfo {

    private Long userId;
    private int level;
    private String username;
    private String pusername;
    private BigDecimal balance;
    private BigDecimal totalRechargeMoney;
    private BigDecimal totalTkMoney;
    private LocalDateTime createTime;
}
