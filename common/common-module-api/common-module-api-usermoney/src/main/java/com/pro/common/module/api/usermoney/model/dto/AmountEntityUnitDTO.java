package com.pro.common.module.api.usermoney.model.dto;

import com.pro.common.module.api.usermoney.model.enums.EnumAmountNegativeDeal;
import com.pro.common.module.api.usermoney.model.modelbase.intf.IAmountEntityRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 数额变化的基础类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmountEntityUnitDTO<T extends IAmountEntityRecord> {
    @ApiModelProperty("数额不足时的处理方式")
    private EnumAmountNegativeDeal negativeDeal = EnumAmountNegativeDeal.throwException;
    @ApiModelProperty("公共信息")
    private T recordCommonInfo;
    @ApiModelProperty("数额变动")
    private List<T> recordList;
    @ApiModelProperty("是否需要变动记录")//还是只需要当前总数值
    private Boolean needSaveRecords = true;

    public AmountEntityUnitDTO(List<T> recordList) {
        this.recordList = recordList;
    }
}
