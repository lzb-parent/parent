package com.pro.common.module.api.system.model.db;

import com.pro.common.modules.api.dependencies.model.classes.BaseConfigModel;
import com.pro.common.modules.api.dependencies.model.classes.IOpenConfigClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典表
 *
 * @author admin
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "字典配置")
@JTDTable(module = "sys", entityId = 107, sequences = {"UNIQUE KEY `code` (`code`)"})
public class AuthDict extends BaseConfigModel implements IAuthDict, IOpenConfigClass {
    @Schema(description = "参数名")
    @JTDField(disabled = true)
    private String label;
    @Schema(description = "编号")
    private String code;
    @Schema(description = "上级")
    @JTDField(entityClass = AuthDict.class, entityClassLabel = "label")
    private String pcode;
    @Schema(description = "参数值")
    //type = JTDConst.EnumFieldType.text
    @JTDField(mainLength = 2048)
    private String value;
    @Schema(description = "输入类型")
    @JTDField(defaultValue = "text")
    private JTDConst.EnumFieldUiType inputType;
    @Schema(description = "枚举类")
    private String enumClass;
    @Schema(description = "枚举类多选")
    private Boolean enumClassMultiple;
    @Schema(description = "描述")
    @JTDField(type = JTDConst.EnumFieldType.text, uiType = JTDConst.EnumFieldUiType.textarea, disabled = true)
    private String remark;
    @Schema(description = "用户端读取")
    @JTDField(defaultValue = "1")
    private Boolean showUser;
    @Schema(description = "管理端显示")
    @JTDField(defaultValue = "1") // 项目中后期时改为 0
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
