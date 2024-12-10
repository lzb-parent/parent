
package com.pro.common.module.api.common.model.db;

import com.pro.common.module.api.common.model.enums.EnumPosterContainerType;
import com.pro.common.module.api.common.model.enums.EnumPosterContentType;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.classes.IConfigClass;
import com.pro.framework.api.enums.IEnumToDbDb;
import com.pro.framework.api.enums.IEnumToDbDbCode;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 文章
 *
 * @author admin
 */
@Data

@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Poster对象", description = "文章")
@JTDTable(sequences = {
        "UNIQUE KEY `uk_code_lang` (`code`,`lang`)"
}, entityId = 324)
public class Poster extends BaseModel implements IConfigClass, IEnumToDbDbCode {

    @ApiModelProperty(value = "分类_目录")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private Long cid;
    @ApiModelProperty(value = "父文章id")//为空表示是顶级文章
    @JTDField(defaultValue = "0")//默认根目录
    private Long pid;
    @ApiModelProperty(value = "文章编号")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null, entityClass = PosterCode.class)
    private String code;
    @ApiModelProperty(value = "语言")
    @JTDField(defaultValue = CommonConst.Str.DEFAULT_LANG_EN, entityClass = Country.class, entityClassKey = "langCode")
    private String lang;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "副标题")
    @JTDField(mainLength = 1000, notNull = JTDConst.EnumFieldNullType.can_null)
    private String subtitle;
    @ApiModelProperty(value = "标签")
    private String tags;
    @ApiModelProperty(value = "作者")
    private String author;
    @ApiModelProperty(value = "图片")
    @JTDField(uiType = JTDConst.EnumFieldUiType.image)
    private String pic;
    @ApiModelProperty(value = "外部链接")
    private String url;
    @ApiModelProperty(value = "视频地址")
    private String video;
    @ApiModelProperty(value = "图标编号")
    private String icon;
    @JTDField(mainLength = 2000, uiType = JTDConst.EnumFieldUiType.textarea)
    @ApiModelProperty(value = "简介")
    private String intro;
    @ApiModelProperty(value = "详情内容类型")
    private EnumPosterContentType contentType;
    @JTDField(type = JTDConst.EnumFieldType.longtext, uiType = JTDConst.EnumFieldUiType.richText)
    @ApiModelProperty(value = "详情")
    private String content;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "启用")
    private Boolean enabled;
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null, uiType = JTDConst.EnumFieldUiType.datetime)
    @ApiModelProperty(value = "上架时间")
    protected String publishTime;
    @ApiModelProperty(value = "备注")
    @JTDField(mainLength = 1024)
    private String remark;

    @ApiModelProperty("列表查询时,是否带有富文本内容")
    transient private Boolean selectContentFlag = false;
    transient private List<String> codes;


    // 扩展功能
    @ApiModelProperty(value = "确认按钮名称")// title||$t('签到') + confirmButtonText||'' 记录为帐变名称
    @JTDField(defaultValue = "确认")
    private String confirmButtonText;
    @ApiModelProperty(value = "确认按钮关联帐变名称")// title||$t('签到') + confirmButtonText||'' 记录为帐变名称
    private String confirmButtonTradeNameText;
    @ApiModelProperty(value = "确认按钮名称")
    private String confirmButtonFinishText;
    @ApiModelProperty(value = "确认按钮路径")
    private String confirmButtonUrl;
    @ApiModelProperty(value = "取消按钮名称")
    private String cancelButtonText;
    @ApiModelProperty(value = "取消按钮路径")
    private String cancelButtonUrl;
    @ApiModelProperty(value = "取消内容")
    @JTDField(type = JTDConst.EnumFieldType.text, uiType = JTDConst.EnumFieldUiType.richText)
    private String cancelButtonContent;
    @ApiModelProperty(value = "父元素样式")
    private String parentStyle;
    @ApiModelProperty(value = "特定页面植入插件")
    @JTDField(type = JTDConst.EnumFieldType.text)
    //例如 显示手机号_验证码功能(验证码类型为指定类型) cmd:appendPhoneSmsCode:connectToBank1  cmd:poster:noReminder
    private String cmds;
    @ApiModelProperty(value = "不再提示开关")
    private Boolean noReminder;
    @ApiModelProperty(value = "最小转圈延迟展示时间")
    private Integer delaySecondStart;
    @ApiModelProperty(value = "最大转圈延迟展示时间")
    private Integer delaySecondEnd;
    @ApiModelProperty(value = "是否溢出弹窗")
    private Boolean overflowFlag;
    @ApiModelProperty(value = "选项数值")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String optionValue;
    @ApiModelProperty(value = "选项内容")
    @JTDField(type = JTDConst.EnumFieldType.text)
    private String optionContent;
    @ApiModelProperty(value = "选项内容2")
    @JTDField(type = JTDConst.EnumFieldType.text)
    private String optionContent2;
    @ApiModelProperty(value = "选项是否可选")
    @JTDField(defaultValue = "1")
    private Boolean optionValidFlag;
    @ApiModelProperty(value = "文章容器样式类型")
    private EnumPosterContainerType containerType;
    @ApiModelProperty(value = "开放关闭窗口静置秒数")
    private Integer closeDelaySecond;
    @ApiModelProperty(value = "特定位置文章可引用参数") // List<PosterEntity> 的json串
//    @JTDField(mainLength = 1024)
    private String paramEntitys;
}
