package com.pro.common.modules.api.dependencies.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.pro.framework.api.model.IModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseModel implements IModel {
    @Serial
    private static final long serialVersionUID = 10000L;

    public static final String field_createTime = "createTime";
    public static final String field_updateTime = "updateTime";
    public static final String field_deleted = "deleted";

    @Schema(description = "主键id")
    @TableId
    protected Long id;

    @Schema(description = "创建时间")
    protected LocalDateTime createTime;

    @Schema(description = "更新时间")
    protected LocalDateTime updateTime;

    @Schema(description = "删除")
    protected Boolean deleted;
}
