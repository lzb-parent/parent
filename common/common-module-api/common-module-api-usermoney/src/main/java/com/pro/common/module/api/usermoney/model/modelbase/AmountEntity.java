package com.pro.common.module.api.usermoney.model.modelbase;

import com.pro.common.module.api.usermoney.model.modelbase.intf.IAmountEntity;
import com.pro.common.modules.api.dependencies.model.BaseUserModel;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty("当前数量")
    private BigDecimal amount;

    @ApiModelProperty("本日增加数额")
    private BigDecimal todayIncreaseAmount;
    @ApiModelProperty("本周增加数额")
    private BigDecimal weeklyIncreaseAmount;
    @ApiModelProperty("本月增加数额")
    private BigDecimal monthIncreaseAmount;
    @ApiModelProperty("本年增加数额")
    private BigDecimal yearIncreaseAmount;
    @ApiModelProperty("历史增加数额")
    private BigDecimal totalIncreaseAmount;

    @ApiModelProperty("本日减少数额")
    private BigDecimal todayDecreaseAmount;
    @ApiModelProperty("本周减少数额")
    private BigDecimal weeklyDecreaseAmount;
    @ApiModelProperty("本月减少数额")
    private BigDecimal monthDecreaseAmount;
    @ApiModelProperty("本年减少数额")
    private BigDecimal yearDecreaseAmount;
    @ApiModelProperty("历史减少数额")
    private BigDecimal totalDecreaseAmount;

    @ApiModelProperty("本日增加次数")
    private Integer todayIncreaseTimes;
    @ApiModelProperty("本周增加次数")
    private Integer weeklyIncreaseTimes;
    @ApiModelProperty("本月增加次数")
    private Integer monthIncreaseTimes;
    @ApiModelProperty("本年增加次数")
    private Integer yearIncreaseTimes;
    @ApiModelProperty("历史增加次数")
    private Integer totalIncreaseTimes;

    @ApiModelProperty("本日减少次数")
    private Integer todayDecreaseTimes;
    @ApiModelProperty("本周减少次数")
    private Integer weeklyDecreaseTimes;
    @ApiModelProperty("本月减少次数")
    private Integer monthDecreaseTimes;
    @ApiModelProperty("本年减少次数")
    private Integer yearDecreaseTimes;
    @ApiModelProperty("历史减少次数")
    private Integer totalDecreaseTimes;

    @Override
    public void setEntityKey(String key) {
        setUserId(Long.valueOf(key));
    }

    @Override
    public String getEntityKey() {
        return String.valueOf(getUserId());
    }

    @ApiModelProperty("更变前的初始值当前数额")
    transient private BigDecimal amountChangeInit;
    @ApiModelProperty("更变前的初始值当前数额")
    transient private Map propMap;

    public <T> Map<String, Function<T,Object>> getPropMap() {
        return propMap;
    }
}
