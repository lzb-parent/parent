package com.pro.common.modules.api.dependencies.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

@Data
public class WriteFileRequest {
    @Schema(description = "内容")
    private String content;
    @Schema(description = "输入路径")
    private String fromPath;
    @Schema(description = "输出路径")
    private String toPath;
    @Schema(description = "替换内容")
    private Map<String,String> replaceMap;
}
