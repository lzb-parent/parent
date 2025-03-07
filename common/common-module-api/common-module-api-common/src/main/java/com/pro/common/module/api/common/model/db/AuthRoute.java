package com.pro.common.module.api.common.model.db;

import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.common.modules.api.dependencies.model.classes.BaseConfigModel;
import com.pro.common.modules.api.dependencies.enums.EnumAuthRouteType;
import com.pro.common.modules.api.dependencies.model.classes.IOpenConfigClass;
import com.pro.framework.api.enums.IEnumToDbDb;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "菜单路由")
@JTDTable(sequences = {
        "UNIQUE KEY `code` (`code`,`sys_role`)"
})
public class AuthRoute extends BaseConfigModel implements IEnumToDbDb, IOpenConfigClass {
    @Schema(description = "名称")
    @JTDField(disabled = true)
    private String name;
    @Schema(description = "编号")
    private String code;
    @Schema(description = "系统角色")//admin/agent/user
    private EnumSysRole sysRole;
    @Schema(description = "上级")
    @JTDField(entityClass = AuthRoute.class)
    private String pcode;
    @Schema(description = "类型")
    private EnumAuthRouteType type;
    @Schema(description = "字体图标")
    private String icon;
    @Schema(description = "图片图标")
    @JTDField(uiType = JTDConst.EnumFieldUiType.image)
    private String pic;
    @Schema(description = "前端路由地址")
    private String componentPath;
    @Schema(description = "后端接口地址权限码")
    private String permissionPaths;
    @Schema(description = "外部链接")
    private String url;
    @Schema(description = "总开关显示")//显示太乱了,强制不显示
    @JTDField(defaultValue = "1")
    private Boolean showFlag;
    @Schema(description = "管理端显示")//功能完整,但未收费隐藏
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
