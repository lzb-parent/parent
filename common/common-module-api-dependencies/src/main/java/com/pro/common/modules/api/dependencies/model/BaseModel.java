package com.pro.common.modules.api.dependencies.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.pro.framework.api.model.IModel;
import com.pro.framework.javatodb.annotation.JTDField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseModel implements IModel {
    private static final long serialVersionUID = 10000L;

    public static final String field_createTime = "createTime";
    public static final String field_updateTime = "updateTime";
    public static final String field_deleted = "deleted";

    @ApiModelProperty(value = "主键id", notes = "主键idDesc")
    @TableId
    protected Long id;

    @ApiModelProperty(value = "创建时间")
    protected LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    protected LocalDateTime updateTime;

    @ApiModelProperty(value = "删除")
    protected Boolean deleted;
}
