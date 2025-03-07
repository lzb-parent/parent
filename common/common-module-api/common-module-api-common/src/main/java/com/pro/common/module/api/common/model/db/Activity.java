//package com.pro.common.module.api.common.model.db;
//
//import com.pro.common.modules.api.dependencies.model.classes.IConfigClass;
//import com.pro.common.modules.api.dependencies.model.BaseModel;
//import com.pro.framework.javatodb.annotation.JTDField;
//import com.pro.framework.javatodb.annotation.JTDTable;
//import com.pro.framework.javatodb.constant.JTDConst;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.media.Schema;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//
//@Data
//
//@EqualsAndHashCode(callSuper = true)
//@Schema(value = "Activity对象", description = "活动")
//@JTDTable(entityId = 301)
//public class Activity extends BaseModel implements IConfigClass {
//
//    @Schema(description = "标题")
//    private String title;
//    @Schema(description = "图片")
//    @JTDField(uiType = JTDConst.EnumFieldUiType.image)
//    private String pic;
//    @JTDField(mainLength = 2000, uiType = JTDConst.EnumFieldUiType.textarea)
//    @Schema(description = "简介")
//    private String intro;
//    @JTDField(type = JTDConst.EnumFieldType.longtext, uiType = JTDConst.EnumFieldUiType.richText)
//    @Schema(description = "内容")
//    private String content;
//    @Schema(description = "链接地址")
//    private String url;
//    @Schema(description = "排序")
//    private Integer sort;
//    @Schema(description = "启用")
//    private Boolean enabled;
//
//
//    //  @Schema(description = "目录")
//////    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
////  private String catalog;
//    @Schema(description = "是否热门")
//    private Boolean isPopular;
//    @Schema(description = "发布时间")
//    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
//    private String publishTime;
//    @Schema(description = "结束时间")
//    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null, uiType = JTDConst.EnumFieldUiType.datetime)
//    private String endTime;
//    @Schema(description = "结束时间标题")
//    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
//    private String endTimeTitle;
//}
