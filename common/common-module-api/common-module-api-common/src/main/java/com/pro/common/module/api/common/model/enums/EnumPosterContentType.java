package com.pro.common.module.api.common.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumPosterContentType {
    richText("富文本"),
    original("原文"),
    ;

    String label;
}
