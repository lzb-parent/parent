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
    @ApiModelProperty(value = "编号")
    private String code;
    /**
     * 两张表对应实体名称 例如 user userMoney
     */
    @ApiModelProperty(value = "实体1 例如 user")
    private String className1;
    @ApiModelProperty(value = "实体2 例如 userMoney")
    private String className2;


    /***
     * 一对一 一对多 多对一 多对多
     */
    @ApiModelProperty(value = "实体1_是1还是多")
    private ClassRelationOneOrManyEnum class1OneOrMany;
    @ApiModelProperty(value = "实体2_是1还是多")
    private ClassRelationOneOrManyEnum class2OneOrMany;

    /***
     * 关系中 是否是否,表1一定该有数据/表2一定该有数据
     */
    @ApiModelProperty(value = "实体1_必填")
    private Boolean class1Require;
    @ApiModelProperty(value = "实体2_必填")
    private Boolean class2Require;

    /***
     * 两个表关联的字段
     */
    @ApiModelProperty(value = "实体1_关联字段")
    private String class1KeyProp;
    @ApiModelProperty(value = "实体2_关联字段")
    private String class2KeyProp;

    @ApiModelProperty(value = "更新时间")
    protected LocalDateTime updateTime;

    public Set<String> getClassNames() {
        return new HashSet<>(Arrays.asList(className1, className2));
    }
}
