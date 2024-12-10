//package com.pro.common.module.api.common.model.db;
//
//import com.pro.common.modules.api.dependencies.model.classes.IConfigClass;
//import com.pro.common.modules.api.dependencies.model.BaseModel;
//import com.pro.framework.javatodb.annotation.JTDField;
//import com.pro.framework.javatodb.annotation.JTDTable;
//import com.pro.framework.javatodb.constant.JTDConst;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//
//@Data
//
//@EqualsAndHashCode(callSuper = true)
//@ApiModel(value = "Activity对象", description = "活动")
//@JTDTable(entityId = 301)
//public class Activity extends BaseModel implements IConfigClass {
//
//    @ApiModelProperty(value = "标题")
//    private String title;
//    @ApiModelProperty(value = "图片")
//    @JTDField(uiType = JTDConst.EnumFieldUiType.image)
//    private String pic;
//    @JTDField(mainLength = 2000, uiType = JTDConst.EnumFieldUiType.textarea)
//    @ApiModelProperty(value = "简介")
//    private String intro;
//    @JTDField(type = JTDConst.EnumFieldType.longtext, uiType = JTDConst.EnumFieldUiType.richText)
//    @ApiModelProperty(value = "内容")
//    private String content;
//    @ApiModelProperty(value = "链接地址")
//    private String url;
//    @ApiModelProperty(value = "排序")
//    private Integer sort;
//    @ApiModelProperty(value = "启用")
//    private Boolean enabled;
//
//
//    //  @ApiModelProperty(value = "目录")
//////    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
////  private String catalog;
//    @ApiModelProperty(value = "是否热门")
//    private Boolean isPopular;
//    @ApiModelProperty(value = "发布时间")
//    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
//    private String publishTime;
//    @ApiModelProperty(value = "结束时间")
//    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null, uiType = JTDConst.EnumFieldUiType.datetime)
//    private String endTime;
//    @ApiModelProperty(value = "结束时间标题")
//    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
//    private String endTimeTitle;
//}
