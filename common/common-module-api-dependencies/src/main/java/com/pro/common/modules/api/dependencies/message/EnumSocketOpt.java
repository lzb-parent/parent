package com.pro.common.modules.api.dependencies.message;

import com.pro.framework.api.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据库操作
 */
@Getter
@AllArgsConstructor
public enum EnumSocketOpt implements IEnum {
//    selectPage(""),//IMultiPageResult<T>
//    selectCountSum(""),//List<AggregateResult>
//    selectList(""),//List<T>
//    selectOne(""),//T
//    selectById(""),//T
//    insertOrUpdate(""),//T
    doInsert("新增记录推送"),//T
    doUpdate("修改记录推送"),//Boolean
    doDelete("删除记录推送"),//Boolean
    ;

    final String label;

}
