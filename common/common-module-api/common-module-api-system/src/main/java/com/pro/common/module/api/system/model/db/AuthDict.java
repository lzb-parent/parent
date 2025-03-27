package com.pro.common.module.api.system.model.db;

import com.pro.common.modules.api.dependencies.model.classes.BaseConfigModel;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典表
 *
 * @author admin
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "字典配置")
@JTDTable(module = "sys", entityId = 107, sequences = {"UNIQUE KEY `code` (`code`)"})
public class AuthDict extends BaseConfigModel implements IAuthDict {
    @ApiModelProperty(value = "参数名")
    @JTDField(disabled = true)
    private String label;
    @ApiModelProperty(value = "编号")
    private String code;
    @ApiModelProperty(value = "上级")
    @JTDField(entityClass = AuthDict.class, entityClassLabel = "label")
    private String pcode;
    @ApiModelProperty(value = "参数值")
    //type = JTDConst.EnumFieldType.text
    @JTDField(mainLength = 2048)
    private String value;
    @ApiModelProperty(value = "输入类型")
    @JTDField(defaultValue = "text")
    private JTDConst.EnumFieldUiType inputType;
    @ApiModelProperty(value = "枚举类")
    private String enumClass;
    @ApiModelProperty(value = "枚举类多选")
    private Boolean enumClassMultiple;
    @ApiModelProperty(value = "描述")
    @JTDField(type = JTDConst.EnumFieldType.text, uiType = JTDConst.EnumFieldUiType.textarea, disabled = true)
    private String remark;
    @ApiModelProperty(value = "用户端读取")
    @JTDField(defaultValue = "1")
    private Boolean showUser;
    @ApiModelProperty(value = "管理端显示")
    @JTDField(defaultValue = "1") // todo 项目中后期时改为 0
    private Boolean showAdmin;

    @Override
    public void setEnumToDbCode(String code) {
        this.code = code;
    }

    @Override
    public String getEnumToDbCode() {
        return code;
    }
}
