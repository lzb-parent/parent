package com.pro.common.module.service.message.service;

import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.enums.EnumAuthRouteType;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.framework.api.enums.EnumToDbEnum;
import com.pro.framework.api.enums.IEnumToDbEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import static com.pro.common.modules.api.dependencies.enums.EnumAuthRouteType.*;
import static com.pro.common.modules.api.dependencies.enums.EnumSysRole.ADMIN;
@Getter
@AllArgsConstructor
@EnumToDbEnum(entityClass = "com.pro.common.module.api.common.model.db.AuthRoute")
public enum EnumAuthRouteMessage implements IEnumToDbEnum {
    catalog_content(ADMIN, null, CATALOG, "站点文案管理", null, "el-icon-document", null, null, null, null, null, 180000, true, null),
    sysMsgChannelMerchant(ADMIN, catalog_content, MENU, "消息通道商户", "/content/sysMsgChannelMerchant", null, null, null, null, null, null, 180600, true, null),
    sysMsgChannelMerchant_QUERY(ADMIN, sysMsgChannelMerchant, BUTTON, "查询", null, null, "sysMsgChannelMerchant", null, null, null, null, 180600, true, null),
    sysMsgChannelMerchant_ALL(ADMIN, sysMsgChannelMerchant, BUTTON, "管理", null, null, "#ALL#sysMsgChannelMerchant", null, null, null, null, 180601, true, null),
    sysMsgChannelTemplate(ADMIN, catalog_content, MENU, "消息通道模板", "/content/sysMsgChannelTemplate", null, null, null, null, null, null, 180700, true, null),
    sysMsgChannelTemplate_QUERY(ADMIN, sysMsgChannelTemplate, BUTTON, "查询", null, null, "sysMsgChannelTemplate", null, null, null, null, 180700, true, null),
    sysMsgChannelTemplate_ALL(ADMIN, sysMsgChannelTemplate, BUTTON, "管理", null, null, "#ALL#sysMsgChannelTemplate", null, null, null, null, 180701, true, null),
    sysMsgRecord(ADMIN, catalog_content, MENU, "消息记录", "/content/sysMsgRecord", null, null, null, null, null, null, 180800, true, null),
    sysMsgRecord_QUERY(ADMIN, sysMsgRecord, BUTTON, "查询", null, null, "sysMsgRecord", null, null, null, null, 180800, true, null),
    sysMsgRecord_ALL(ADMIN, sysMsgRecord, BUTTON, "管理", null, null, "#ALL#sysMsgRecord", null, null, null, null, 180801, true, null),

    ;
    private final EnumSysRole sysRole;
    private final EnumAuthRouteMessage pcode;
    private final EnumAuthRouteType type;
    private final String name;
    private final String componentPath;
    private final String icon;
    private final String permissionPaths;
    private final String pic;
    private final String url;
    private final String remark;
    private final Boolean enabled;
    private final Integer sort;
    private final Boolean showFlag;
    /**
     * 统一重置修改掉,这个配置时间以前的,旧的配置
     */
    private final String forceChangeTime;

    @Override
    public String getToDbCode() {
        return sysRole + CommonConst.Str.SPLIT + name();
    }
}
