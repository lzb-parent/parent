package com.pro.common.module.api.usermoney.model.modelbase;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 数额变化的基础类
 */
@Data
@SuperBuilder
@NoArgsConstructor
public abstract class AmountEntityRecordDTO extends AmountEntityRecordBaseDTO {
}

