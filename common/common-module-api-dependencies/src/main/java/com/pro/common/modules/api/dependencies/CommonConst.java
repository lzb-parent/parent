package com.pro.common.modules.api.dependencies;

import java.math.BigDecimal;

/**
 * 只能依赖jdk
 */
public class CommonConst {

    public static class CacheKey {
        public static final String login = "login";
        public static final String AuthDict = "AuthDict";
        public static final String UserLevelConfig = "UserLevelConfig";
        public static final String LoginInfo = "LoginInfo";
        public static final String SmsCode = "SmsCode";//验证码
        /**
         * 验证码
         */
        public static final String CHECK_CODE = "common:checkCode";
    }

    public static class TopicAppend {
        public static final String ToSocket = "ToSocket";
    }
    public static class Bean {
        public static final String userService = "userService";
        public static final String agentService = "agentService";
        public static final String adminService = "adminService";
//        public static final String userMoneyService = "userMoneyService";
    }

    //    public enum CacheableKey {
//        login,
//        ;
//    }
    public static class EntityClass {
        public static final String SysMsgRecord = "SysMsgRecord"; // 系统通知
        public static final String UserWithdraw = "UserWithdraw"; // 用户提款记录
        public static final String UserRecharge = "UserRecharge"; // 用户充值记录
    }
    public static class ToSocketUrl {
        // 用户端通知，用户端需要加上/user前缀
//        public static final String getUserInfo = "/getUserInfo"; // 更新用户信息
//        public static final String getUserMoneyInfo = "/getUserMoney"; // 更新用户金额
//        public static final String getUserLevel = "/getUserLevel"; // 更新用户等级
        public static final String getUserNotice = "/getUserNotice"; // 系统通知

//        public static final String getUserNoticeNum = "/getUserNoticeNum"; // 系统通知未读数量更新
//        public static final String getOrderNumUserPay = "/getOrderNumUserPay"; // 系统通知未读数量更新
//        public static final String receiveChatGroupRecord = "/receiveChatGroupRecord"; // 收到消息
//        public static final String readChatGroupRecords = "/readChatGroupRecords"; // 消息改为已读
//        public static final String getUserFirstRecharge = "/getUserFirstRecharge";// 推送用户首次充值
//
//        // 交易所更新通知
////    public static final String getTradeProductList = "/getTradeProductList"; // 产品列表更新
//        public static final String getCurrentPrice = "/getCurrentPrice";
//
//        // 数据更新通知
//        public static final String getProfitData = "/getProfitData"; // 收益数据
//        public static final String getNumberData = "/getNumberData"; // 参与人数
//
//        // 管理后台通知
//        public static final String getWithdrawMsg = "/getWithdrawMsg";// 接收提现通知声音
        public static final String getWithdrawList = "/getWithdrawList";// 接收提现列表
        public static final String getRechargeList = "/getRechargeList";// 接收充值列表
//        public static final String getRechargeAmount = "/getRechargeAmount";// 接收充值额度推送
//
//        // 全量强制更新
//        public static final String refreshDict = "/refreshDict"; // 强制更新字典缓存
//        public static final String refreshUiUser = "/refreshUiUser"; // 强制更新用户端
//        public static final String refreshUiAdmin = "/refreshUiAdmin"; // 强制更新管理端
//        public static final String refreshUiAgent = "/refreshUiAgent"; // 强制更新代理端
//        public static final String refreshSysNotice = "/refreshSysNotice"; // 更新系统弹窗公告

    }

    public static class Reg {
        public static final String EMAIL = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}";
    }

    public static class Str {
        public static final String CACHE_KEY_R = "";
        public static final String UNDERLINE = "_";

        // 英语 简体中文 繁体中文
        public static final String LANG_EN = "en_US";
        public static final String LANG_ZH_CN = "zh_CN";
        public static final String LANG_zh_TW = "zh_TW";

        // 图形验证码key
        public static final String SYSTEM_CAPTCHA = "SYSTEM_CAPTCHA";
        public static final String path_loginUserIds = "/loginUserIds";

        public static final String DEFAULT_LANG_EN = "en-US";
        public static final String SPLIT = "##";
        public static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    }

    public static class Num {
        public static final Integer NO = 0;
        public static final Integer YES = 1;
        public static final BigDecimal hundred = new BigDecimal("100");
    }
}
