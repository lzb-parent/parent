package com.pro.common.web.security.service;

import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import com.pro.common.modules.api.dependencies.model.LoginRequest;
import com.pro.common.modules.api.dependencies.service.IAuthRoleService;
import com.pro.common.modules.api.dependencies.service.ILoginInfoService;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.framework.api.util.AssertUtil;
import com.pro.framework.api.util.JSONUtils;
import com.pro.framework.api.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommonLoginService {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CommonProperties commonProperties;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private IAuthRoleService authRoleService;
//    @Autowired
//    private CacheManager cacheManager;

    @Transactional
    public String login(String requestStr) {
        LoginRequest request = JSONUtils.fromString(requestStr, LoginRequest.class);
        // 查询 userService / adminService /agentService
        ILoginInfo loginInfo = this.getLoginInfoService().doLogin(request);
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
        authRoleService.clearCache(commonProperties.getApplication().getRole(), loginInfo.getId());
        // 返回登录token
        return tokenService.generate(loginInfo);
    }

    @Transactional
    public String register(String request, String ip, String lang) {
        // 注册
        ILoginInfo loginInfo = this.getLoginInfoService().register(request, ip, lang);
        // 返回登录token
        return tokenService.generate(loginInfo);
    }

    private ILoginInfoService getLoginInfoService() {
        return applicationContext.getBean(commonProperties.getApplication().getRole().getServiceBean(), ILoginInfoService.class);
    }

    public ILoginInfo getLoginInfo(Long id) {
        return this.getLoginInfoService().getById(id);
    }

    public Boolean getGoogleAuthOpen(LoginRequest request) {
        ILoginInfo loginInfo = this.getLoginInfoService().doLogin(request);
        return null != loginInfo && loginInfo.getGoogleAuthOpen();
    }
}
