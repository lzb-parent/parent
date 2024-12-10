package com.pro.common.module.api.system.model.db;

import com.pro.framework.api.enums.IEnumToDbDb;
import com.pro.framework.javatodb.constant.JTDConst;

/**
 * 字典表
 *
 * @author admin
 */
public interface IAuthDict extends IEnumToDbDb {
    String getPcode();

    String getCode();

    String getLabel();

    String getValue();

    JTDConst.EnumFieldUiType getInputType();

    Boolean getShowUser();

    Boolean getShowAdmin();

    String getEnumClass();

    Boolean getEnumClassMultiple();

    String getRemark();
}
