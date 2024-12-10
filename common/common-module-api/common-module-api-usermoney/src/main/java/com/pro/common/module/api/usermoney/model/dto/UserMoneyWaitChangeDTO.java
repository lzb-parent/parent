package com.pro.common.module.api.usermoney.model.dto;

import com.pro.common.module.api.usermoney.model.modelbase.AmountEntityRecordDTO;
import com.pro.framework.api.FrameworkConst;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserMoneyWaitChangeDTO extends AmountEntityRecordDTO {

    @ApiModelProperty("下次状态更变时间")
    private LocalDateTime nextStateTime;

    //    @Override
    public void setEntityKey(String key) {
        setAmountType(key.split(FrameworkConst.Str.split_pound)[0]);
        setUserId(Long.valueOf(key.split(FrameworkConst.Str.split_pound)[1]));
    }
}
