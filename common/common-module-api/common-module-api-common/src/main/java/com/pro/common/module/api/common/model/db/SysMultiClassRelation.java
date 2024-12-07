package com.pro.common.module.api.common.model.db;

import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.classes.IAdminClass;
import com.pro.framework.api.model.IdModel;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.mtq.service.multiwrapper.entity.ClassRelationOneOrManyEnum;
import com.pro.framework.mtq.service.multiwrapper.entity.IMultiClassRelation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 类和类的关系
 * 可以在数据库中储存(推荐),也可以从枚举中读取
 * 可以,统一按表名字排序(class1和class2,不同的Relation和Relation)
 *
 * @author Administrator
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ApiModel(value = "系统表关系")
@JTDTable(value = "系统表关系", keyFieldNames = "code", entityId = 334)
public class SysMultiClassRelation extends BaseModel implements IMultiClassRelation, IAdminClass {
    /**
     * 表关系唯一编号,例如 userAndUserStaff
     */
//    @TableId(type = IdType.INPUT)
    private String code;

//    /**
//     * 两张表名 (仅做展示用)
//     */
//    private Class<?> class1;
//    private Class<?> class2;
    /**
     * 两张表对应实体名称 例如 user userMoney
     */
    private String className1;
    private String className2;

    public Set<String> getClassNames() {
        return new HashSet<>(Arrays.asList(className1, className2));
    }

    /***
     * 一对一 一对多 多对一 多对多
     */
    private ClassRelationOneOrManyEnum class1OneOrMany;
    private ClassRelationOneOrManyEnum class2OneOrMany;

    /***
     * 关系中 是否是否,表1一定该有数据/表2一定该有数据
     */
    private Boolean class1Require;
    private Boolean class2Require;

    /***
     * 两个表关联的字段
     */
    private String class1KeyProp;
    private String class2KeyProp;

    @ApiModelProperty(value = "更新时间")
    protected LocalDateTime updateTime;
}
