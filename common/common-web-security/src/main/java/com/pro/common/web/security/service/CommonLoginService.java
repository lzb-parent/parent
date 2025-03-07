package com.pro.common.web.security.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pro.common.module.api.message.enums.EnumSysMsgBusinessCode;
import com.pro.common.module.api.message.intf.ISysMsgService;
import com.pro.common.module.api.system.model.enums.EnumAuthDict;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.api.dependencies.model.ILoginInfoPrepare;
import com.pro.common.modules.api.dependencies.model.LoginRequest;
import com.pro.common.modules.api.dependencies.service.ILoginInfoService;
import com.pro.common.modules.api.dependencies.user.model.UserMsg;
import com.pro.common.modules.service.dependencies.modelauth.base.AccessToken;
import com.pro.common.modules.service.dependencies.modelauth.base.LoginInfoService;
import com.pro.common.modules.service.dependencies.modelauth.base.TokenService;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.common.modules.service.dependencies.util.IPUtils;
import com.pro.framework.api.cache.ICacheManagerCenter;
import com.pro.framework.api.util.AssertUtil;
import com.pro.framework.api.util.JSONUtils;
import com.pro.framework.api.util.PasswordUtils;
import com.pro.framework.api.util.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
public class CommonLoginService {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CommonProperties commonProperties;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private LoginInfoService loginInfoService;
    @Autowired
    private ISysMsgService sysMsgService;
    @Autowired
    private ICacheManagerCenter cacheManagerCenter;

    @Transactional
    public AccessToken login(String requestStr) {
        LoginRequest request = JSONUtils.fromString(requestStr, LoginRequest.class);
        // 查询 userService / adminService /agentService
        ILoginInfoPrepare loginInfo = this.getLoginInfoService().doLogin(request);
        if (loginInfo == null) {
//            I18nUtils.throwBusinessException("用户不存在", request.getUsername());
            throw new BusinessException("用户不存在_", request.getUsername());
        }
        AssertUtil.notEmpty(loginInfo, "用户不存在_", request.getUsername());
        AssertUtil.isTrue(loginInfo.getEnabled(), "区域限制_无法登陆");


        // 密码生成器
        if (!loginInfo.getPassword().equals(PasswordUtils.encrypt_Password(request.getPassword()))) {
            throw new BusinessException("密码错误");
        }
        // 重载(清理)菜单缓存
//        loginInfoService.clearCache(commonProperties.getApplication().getRole(), loginInfo.getId());
        // 返回登录token
        return tokenService.generate(loginInfo);
    }

    @Transactional
    public AccessToken register(String request, String ip, String lang) {
        // 注册
        ILoginInfoPrepare loginInfo = this.getLoginInfoService().register(request, ip, lang);
        // 返回登录token
        return tokenService.generate(loginInfo);
    }

    private ILoginInfoService getLoginInfoService() {
        return applicationContext.getBean(commonProperties.getApplication().getRole().getServiceBean(), ILoginInfoService.class);
    }

    public ILoginInfoPrepare getLoginInfo(Long id) {
        return this.getLoginInfoService().getById(id);
    }

    public Boolean getGoogleAuthOpen(LoginRequest request) {
        ILoginInfoPrepare loginInfo = this.getLoginInfoService().doLogin(request);
        return null != loginInfo && loginInfo.getGoogleAuthOpen();
    }

    @Transactional
    public AccessToken register(HttpServletRequest request, String captchaInner, String requestBody) {
        JSONObject jo = JSONUtil.parseObj(requestBody);
        // 图形验证码
        if (EnumAuthDict.REGISTER_PROPS.getValueCache().contains(",captcha,")) {
            String captcha = jo.getStr("captcha");
            if (StrUtils.isBlank(captcha)) {
                throw new BusinessException("图形验证码不正确");
            } else if (!captcha.equals(captchaInner)) {
                throw new BusinessException("图形验证码不正确");
            }
        }
        if (EnumAuthDict.REGISTER_PROPS.getValueCache().contains(",smsCode,")) {
            String codeInput = jo.getStr("smsCode");
            String key = sysMsgService.getMsgKey(jo.toBean(UserMsg.class), EnumSysMsgBusinessCode.REGISTER_CODE.name());
            String code = (String) cacheManagerCenter.get(CommonConst.CacheKey.SmsCode, key);
            AssertUtil.isTrue(StrUtil.isNotBlank(code) && code.equals(codeInput), "短信或邮箱验证码不正确");
        }


        String ip = IPUtils.getIpAddress(request);
        // 返回token
        return this.register(requestBody, ip, LocaleContextHolder.getLocale().toLanguageTag());
    }
}
