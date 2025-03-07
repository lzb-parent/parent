package com.pro.common.module.api.usermoney.model.modelbase;

import com.pro.framework.api.FrameworkConst;
import com.pro.framework.javatodb.annotation.JTDField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 数额变化的基础类
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AmountEntityWithType extends AmountEntity {
    @Schema(description = "数额类型")
    @JTDField(defaultValue = FrameworkConst.Str.DEFAULT)
    private String amountType;

    @Override
    public String getEntityKey() {
        return getAmountType() + FrameworkConst.Str.split_pound + getUserId();
    }

    @Override
    public void setEntityKey(String key) {
        this.amountType = key.split(FrameworkConst.Str.split_pound)[0];
        setUserId(Long.valueOf(key.split(FrameworkConst.Str.split_pound)[1]));
    }
}
