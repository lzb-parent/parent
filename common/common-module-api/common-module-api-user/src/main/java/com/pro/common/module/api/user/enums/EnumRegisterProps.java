package com.pro.common.module.api.user.enums;


import com.pro.framework.api.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum EnumRegisterProps implements IEnum {
    username("用户名_账号"),
    password("登录密码"),
    rePassword("登录密码确认"),
    tkPassword("提款密码"),
    inviteCode("邀请码"),
    phone("手机号"),
    realName("真实姓名"),
    nickName("昵称"),
//    telegram("telegram"),
//    whatsapp("whatsapp"),
    email("邮箱"),
    captcha("验证码"),
    smsCode("邮箱或短信验证码"),
    register_and_agree_to_the_terms("注册同意条款"),


    ;
    final String label;
}
