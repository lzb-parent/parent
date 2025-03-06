package com.pro.common.module.api.common.model.db;

import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.common.modules.api.dependencies.model.classes.BaseConfigModel;
import com.pro.common.modules.api.dependencies.enums.EnumAuthRouteType;
import com.pro.framework.api.enums.IEnumToDbDb;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 路由表
 *
 * @author admin
 */
@Data
@ApiModel(description = "菜单路由")
@JTDTable(sequences = {
        "UNIQUE KEY `code` (`code`,`sys_role`)"
})
public class AuthRoute extends BaseConfigModel implements IEnumToDbDb {
    @ApiModelProperty(value = "名称")
    @JTDField(disabled = true)
    private String name;
    @ApiModelProperty(value = "编号")
    private String code;
    @ApiModelProperty(value = "系统角色")//admin/agent/user
    private EnumSysRole sysRole;
    @ApiModelProperty(value = "上级")
    @JTDField(entityClass = AuthRoute.class)
    private String pcode;
    @ApiModelProperty(value = "类型")
    private EnumAuthRouteType type;
    @ApiModelProperty(value = "字体图标")
    private String icon;
    @ApiModelProperty(value = "图片图标")
    @JTDField(uiType = JTDConst.EnumFieldUiType.image)
    private String pic;
    @ApiModelProperty(value = "前端路由地址")
    private String componentPath;
    @ApiModelProperty(value = "后端接口地址权限码")
    private String permissionPaths;
    @ApiModelProperty(value = "外部链接")
    private String url;
    @ApiModelProperty(value = "总开关显示")//显示太乱了,强制不显示
    @JTDField(defaultValue = "1")
    private Boolean showFlag;
    @ApiModelProperty(value = "管理端显示")//功能完整,但未收费隐藏
    @JTDField(defaultValue = "1")
    private Boolean showAdmin;

    @Override
    public void setEnumToDbCode(String code) {
        String[] split = code.split(CommonConst.Str.SPLIT);
        this.sysRole = EnumSysRole.valueOf(split[0]);
        this.code = split[1];
    }

    @Override
    public String getEnumToDbCode() {
        return sysRole + CommonConst.Str.SPLIT + code;
    }
}
