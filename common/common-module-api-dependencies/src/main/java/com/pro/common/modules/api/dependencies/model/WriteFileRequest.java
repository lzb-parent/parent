package com.pro.common.modules.api.dependencies.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
public class WriteFileRequest {
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty("输入路径")
    private String fromPath;
    @ApiModelProperty("输出路径")
    private String toPath;
    @ApiModelProperty("替换内容")
    private Map<String,String> replaceMap;
}
