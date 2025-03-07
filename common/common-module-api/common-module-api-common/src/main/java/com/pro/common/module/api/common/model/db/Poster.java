
package com.pro.common.module.api.common.model.db;

import com.pro.common.module.api.common.model.enums.EnumPosterContainerType;
import com.pro.common.module.api.common.model.enums.EnumPosterContentType;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.classes.IOpenConfigClass;
import com.pro.framework.api.enums.IEnumToDbDbCode;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "文章")
@JTDTable(sequences = {
        "UNIQUE KEY `uk_code_lang` (`code`,`lang`)"
}, entityId = 324)
public class Poster extends BaseModel implements IOpenConfigClass, IEnumToDbDbCode {

    @Schema(description = "分类_目录")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private Long cid;
    @Schema(description = "父文章id")//为空表示是顶级文章
    @JTDField(defaultValue = "0")//默认根目录
    private Long pid;
    @Schema(description = "文章编号")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null, entityClass = PosterCode.class)
    private String code;
    @Schema(description = "语言")
    @JTDField(defaultValue = CommonConst.Str.DEFAULT_LANG_EN, entityClass = Country.class, entityClassKey = "langCode")
    private String lang;
    @Schema(description = "标题")
    private String title;
    @Schema(description = "副标题")
    @JTDField(mainLength = 1000, notNull = JTDConst.EnumFieldNullType.can_null)
    private String subtitle;
    @Schema(description = "标签")
    private String tags;
    @Schema(description = "作者")
    private String author;
    @Schema(description = "图片")
    @JTDField(uiType = JTDConst.EnumFieldUiType.image)
    private String pic;
    @Schema(description = "外部链接")
    private String url;
    @Schema(description = "视频地址")
    private String video;
    @Schema(description = "图标编号")
    private String icon;
    @JTDField(mainLength = 2000, uiType = JTDConst.EnumFieldUiType.textarea)
    @Schema(description = "简介")
    private String intro;
    @Schema(description = "详情内容类型")
    private EnumPosterContentType contentType;
    @JTDField(type = JTDConst.EnumFieldType.longtext, uiType = JTDConst.EnumFieldUiType.richText)
    @Schema(description = "详情")
    private String content;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "启用")
    private Boolean enabled;
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null, uiType = JTDConst.EnumFieldUiType.datetime)
    @Schema(description = "上架时间")
    protected String publishTime;
    @Schema(description = "备注")
    @JTDField(mainLength = 1024)
    private String remark;

    @Schema(description = "列表查询时,是否带有富文本内容")
    transient private Boolean selectContentFlag = false;
    transient private List<String> codes;


    // 扩展功能
    @Schema(description = "确认按钮名称")// title||$t('签到') + confirmButtonText||'' 记录为帐变名称
    @JTDField(defaultValue = "确认")
    private String confirmButtonText;
    @Schema(description = "确认按钮关联帐变名称")// title||$t('签到') + confirmButtonText||'' 记录为帐变名称
    private String confirmButtonTradeNameText;
    @Schema(description = "确认按钮名称")
    private String confirmButtonFinishText;
    @Schema(description = "确认按钮路径")
    private String confirmButtonUrl;
    @Schema(description = "取消按钮名称")
    private String cancelButtonText;
    @Schema(description = "取消按钮路径")
    private String cancelButtonUrl;
    @Schema(description = "取消内容")
    @JTDField(type = JTDConst.EnumFieldType.text, uiType = JTDConst.EnumFieldUiType.richText)
    private String cancelButtonContent;
    @Schema(description = "父元素样式")
    private String parentStyle;
    @Schema(description = "特定页面植入插件")
    @JTDField(type = JTDConst.EnumFieldType.text)
    //例如 显示手机号_验证码功能(验证码类型为指定类型) cmd:appendPhoneSmsCode:connectToBank1  cmd:poster:noReminder
    private String cmds;
    @Schema(description = "不再提示开关")
    private Boolean noReminder;
    @Schema(description = "最小转圈延迟展示时间")
    private Integer delaySecondStart;
    @Schema(description = "最大转圈延迟展示时间")
    private Integer delaySecondEnd;
    @Schema(description = "是否溢出弹窗")
    private Boolean overflowFlag;
    @Schema(description = "选项数值")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String optionValue;
    @Schema(description = "选项内容")
    @JTDField(type = JTDConst.EnumFieldType.text)
    private String optionContent;
    @Schema(description = "选项内容2")
    @JTDField(type = JTDConst.EnumFieldType.text)
    private String optionContent2;
    @Schema(description = "选项是否可选")
    @JTDField(defaultValue = "1")
    private Boolean optionValidFlag;
    @Schema(description = "文章容器样式类型")
    private EnumPosterContainerType containerType;
    @Schema(description = "开放关闭窗口静置秒数")
    private Integer closeDelaySecond;
    @Schema(description = "特定位置文章可引用参数") // List<PosterEntity> 的json串
//    @JTDField(mainLength = 1024)
    private String paramEntitys;
}
