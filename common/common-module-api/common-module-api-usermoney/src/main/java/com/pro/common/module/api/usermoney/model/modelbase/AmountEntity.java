package com.pro.common.module.api.usermoney.model.modelbase;

import com.pro.common.module.api.usermoney.model.modelbase.intf.IAmountEntity;
import com.pro.common.modules.api.dependencies.model.BaseUserModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;

/**
 * 数额变化的基础类
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AmountEntity extends BaseUserModel implements IAmountEntity {

    @Schema(description = "当前数量")
    private BigDecimal amount;

    @Schema(description = "本日增加数额")
    private BigDecimal todayIncreaseAmount;
    @Schema(description = "本周增加数额")
    private BigDecimal weeklyIncreaseAmount;
    @Schema(description = "本月增加数额")
    private BigDecimal monthIncreaseAmount;
    @Schema(description = "本年增加数额")
    private BigDecimal yearIncreaseAmount;
    @Schema(description = "历史增加数额")
    private BigDecimal totalIncreaseAmount;

    @Schema(description = "本日减少数额")
    private BigDecimal todayDecreaseAmount;
    @Schema(description = "本周减少数额")
    private BigDecimal weeklyDecreaseAmount;
    @Schema(description = "本月减少数额")
    private BigDecimal monthDecreaseAmount;
    @Schema(description = "本年减少数额")
    private BigDecimal yearDecreaseAmount;
    @Schema(description = "历史减少数额")
    private BigDecimal totalDecreaseAmount;

    @Schema(description = "本日增加次数")
    private Integer todayIncreaseTimes;
    @Schema(description = "本周增加次数")
    private Integer weeklyIncreaseTimes;
    @Schema(description = "本月增加次数")
    private Integer monthIncreaseTimes;
    @Schema(description = "本年增加次数")
    private Integer yearIncreaseTimes;
    @Schema(description = "历史增加次数")
    private Integer totalIncreaseTimes;

    @Schema(description = "本日减少次数")
    private Integer todayDecreaseTimes;
    @Schema(description = "本周减少次数")
    private Integer weeklyDecreaseTimes;
    @Schema(description = "本月减少次数")
    private Integer monthDecreaseTimes;
    @Schema(description = "本年减少次数")
    private Integer yearDecreaseTimes;
    @Schema(description = "历史减少次数")
    private Integer totalDecreaseTimes;

    @Override
    public void setEntityKey(String key) {
        setUserId(Long.valueOf(key));
    }

    @Override
    public String getEntityKey() {
        return String.valueOf(getUserId());
    }

    @Schema(description = "更变前的初始值当前数额")
    transient private BigDecimal amountChangeInit;
    @Schema(description = "更变前的初始值当前数额")
    transient private Map propMap;

    public <T> Map<String, Function<T,Object>> getPropMap() {
        return propMap;
    }
}
