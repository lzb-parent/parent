package com.pro.common.module.api.system.model.enums;

import com.pro.common.module.api.system.model.db.AuthDict;
import com.pro.framework.api.enums.IEnumToDbEnum;
import com.pro.framework.javatodb.constant.JTDConst;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.pro.framework.javatodb.constant.JTDConst.EnumFieldUiType.*;

/**
 * 字典配置 基础    * 只在定制处执行入库 IEnumToDbEnum<IAuthDict>
 * EnumAuthDict
 */
@Getter
@AllArgsConstructor
public enum EnumDict implements IEnumAuthDict, IEnumToDbEnum<AuthDict> {
    // 系统配置 SYSTEM_CONFIG
    SYSTEM_CONFIG("系统配置", "", null, text, null, null, null, null, null, null, 0, null),
    SITE_NAME("平台名称", "siteName", SYSTEM_CONFIG, text, true, null, null, null, null, null, 1, null),
    SYSTEM_VERSION("系统版本号", "0", SYSTEM_CONFIG, text, true, null, null, null, null, null, 2, null),
    SYSTEM_COUNTRY("系统运营国家代码", "USA", SYSTEM_CONFIG, text, true, null, null, null, null, null, 3, null),
    SYSTEM_COUNTRY_CURRENCY("系统运营国家货币代码", "USD", SYSTEM_CONFIG, text, true, null, null, null, null, null, 4, null),
    SYSTEM_COUNTRY_CURRENCY_SYMBOL("系统运营国家货币符号", "$", SYSTEM_CONFIG, text, true, null, null, null, null, null, 5, null),
    FILE_UPLOAD_DOMAIN("文件服务器地址", "http://localhost:8153/api", SYSTEM_CONFIG, text, true, null, "", false, "", null, 6, null),
    FRONT_DOMAIN("前端域名地址","http://localhost:8153",SYSTEM_CONFIG, text,true,null,null,null,null,null,6,null),
    FRONT_API("前端接口地址","http://localhost:8153/api",SYSTEM_CONFIG, textarea,true,null,null,null,"多个请用,隔开  每个域名后面加上/api",null,6,null),
    APP_DOWNLOAD_OPEN("app下载开关", "1", SYSTEM_CONFIG, bool, true, null, null, null, null, null, 7, null),
    APP_DOWNLOAD_BTN_GONE_SECOND("app下载消失时间", "0", SYSTEM_CONFIG, bool, true, null, null, null, "设置-1一直显示", null, 8, null),
    APP_DOWNLOAD_URL("app下载地址", "/app", SYSTEM_CONFIG, text, true, null, null, null, null, null, 8, null),
    ANDROID_DOWNLOAD_URL("安卓app下载地址", "/app", SYSTEM_CONFIG, text, true, null, null, null, null, null, 9, null),
    IOS_DOWNLOAD_URL("iOS下载地址", "/app", SYSTEM_CONFIG, text, true, null, null, null, null, null, 10, null),
    SYSTEM_ONLY_MOBILE_OPEN("统一手机端界面开关", "1", SYSTEM_CONFIG, bool, true, null, null, null, null, null, 100, null),
    SYSTEM_PC_FORCE_APP("电脑端强制跳到app下载页", "0", SYSTEM_CONFIG, bool, true, null, null, null, null, null, 100, null),
    ADMIN_LANG_CHINESE("后台统一中文", "0", SYSTEM_CONFIG, bool, true, null, "", false, "", null, 100, null),
//    DEV_UI_ADMIN_PATH("开发工具_本地ui_admin源码根目录绝对路径", "/Users/fa/projectnew/snowball/snowball-ui-admin/src/parent-ui-admin", null, text, null, null, null, null, null, null, null, null),
    DEV_PROJECT_ROOT_PATH("开发工具_本地项目根目录", "/Users/zubin/projectNew/snowball", null, text, null, null, null, null, null, null, null, null),

    // 用户端配置 USER_SITE_CONFIG
    USER_SITE_CONFIG("用户端配置", "", null, text, false, null, null, null, null, null, 1, null),
    SYSTEM_NOTICE("系统公告", "This is system notice", USER_SITE_CONFIG, textarea, true, null, "", false, "站点系统公告，显示在首页公告位置", null, null, null),
    SERVICE_ONLINE("在线客服链接", "https://t.me/xxxxxxx", USER_SITE_CONFIG, text, true, null, "", false, "如：https://t.me/fafafa56788", null, null, null),
    USER_LANG_NAME_OPEN("用户端语言名称显示开关", "0", USER_SITE_CONFIG, bool, true, null, "", false, "", null, null, null),

    // 佣金账户配置 COMMISSION_CONFIG
    COMMISSION_CONFIG("佣金账户配置", "", null, text, null, null, null, null, null, null, 1, null),
    COMMISSION_ACCOUNT_OPEN("佣金账户开关", "0", COMMISSION_CONFIG, bool, true, null, null, null, "打开后，启用佣金账户功能", null, 100, null),
    USER_TEAM_LEVELS("用户团队统计层级", "3", COMMISSION_CONFIG, number, true, null, null, null, "", null, 100, null),

    // 注册设置
    REGISTER_CONFIG("注册配置", "", null, text, null, null, null, null, null, null, 1, null),
    REGISTER_PHONE_PATTERN("手机号正则表达式", "", REGISTER_CONFIG, bool, true, null, null, null, "例如 ^09\\d{9}$ 09开头的11位数", null, null, null),
    REGISTER_PROPS("注册属性", "username,password,rePassword,tkPassword,inviteCode", REGISTER_CONFIG, select, true, null, "EnumRegisterProps", true, "", null, null, null),
    REGISTER_PROPS_REQUIRE("注册属性必填", "inviteCode", REGISTER_CONFIG, select, true, null, "EnumRegisterProps", true, "注册必填属性", null, null, null),
    REGISTER_CODE_LETTERS_USER("注册用户推荐码字母", "0123456789", REGISTER_CONFIG, select, true, null, "EnumRegisterProps", true, " 数字+字母 123456789ABCDEFGHIJKLMNPQRSTUVWXYZ", null, null, null),
    REGISTER_CODE_LETTERS_AGENT("注册代理推荐码字母", "0123456789", REGISTER_CONFIG, select, true, null, "EnumRegisterProps", true, "", null, null, null),
    REGISTER_USERNAME_FROM("注册默认用户名来源", "INPUT", REGISTER_CONFIG, select, true, null, "EnumRegisterUsernameFrom", false, "注册时,平台唯一的username取自什么字段", null, null, null),

    // 提款配置 WITHDRAW_CONFIG
    WITHDRAW_CONFIG("提现_银行卡配置", "", null, text, null, null, null, null, null, null, 1, null),
    USER_BANK_CARD_EDIT_OPEN("修改卡号开关", "1", WITHDRAW_CONFIG, null, true, null, null, null, null, null, null, null),
    USER_BANK_CARD_DELETE_OPEN("删除卡号开关", "1", WITHDRAW_CONFIG, null, true, null, null, null, null, null, null, null),
    WITHDRAW_BANK_LIST("银行卡列表", "AGRIBANK\nBAC A BANK\nBAO VIET BANK\nBIDV BANK\nEXIMBANK", WITHDRAW_CONFIG, textarea, null, null, null, null, "一行一个", null, null, null),
    BANK_ACCOUNT_OPEN("银行卡号显示开关", "1", WITHDRAW_CONFIG, bool, null, null, null, null, "一行一个", null, null, null),

    // 支付配置 PAY_CONFIG
    PAY_CONFIG("支付配置", "", null, text, null, null, null, null, null, null, 1, null),
    PAY_URL("支付地址", "http://127.0.0.1:8097", PAY_CONFIG, text, true, null, null, null, null, null, null, null),
    PAY_NOTIFY_URL("支付回调地址", "http://127.0.0.1:8097", PAY_CONFIG, text, null, null, null, null, null, null, null, null),

    // 推送配置 PUSH_CONFIG
    ADMIN_CONFIG("后台配置", "", null, text, null, null, null, null, null, null, 1, null),
    PUSH_ADMIN_RECHARGE_OPEN("后台充值实时推送开关", "1", ADMIN_CONFIG, null, null, null, null, null, null, null, null, null),
    PUSH_ADMIN_WITHDRAW_OPEN("后台提款实时推送开关", "1", ADMIN_CONFIG, null, null, null, null, null, null, null, null, null),

    ;
    private final String label;
    private final String value;
    private final EnumDict pcode;
    private final JTDConst.EnumFieldUiType inputType;
    private final Boolean showUser;
    private final Boolean showAdmin;
    private final String enumClass;
    private final Boolean enumClassMultiple;
    private final String remark;
    private final Boolean enabled;
    private final Integer sort;
    /**
     * 统一重置修改掉,这个配置时间以前的,旧的配置
     */
    private final String forceChangeTime;
}
