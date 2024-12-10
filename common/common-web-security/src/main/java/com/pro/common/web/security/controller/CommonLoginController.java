package com.pro.common.web.security.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pro.common.module.api.message.enums.EnumSysMsgBusinessCode;
import com.pro.common.module.api.system.model.enums.EnumDict;
import com.pro.common.module.api.user.intf.IUserService;
import com.pro.common.module.api.user.model.db.User;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.R;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import com.pro.common.modules.api.dependencies.model.LoginRequest;
import com.pro.common.modules.api.dependencies.service.IAuthRoleService;
import com.pro.common.modules.api.dependencies.user.model.IUser;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.common.modules.service.dependencies.util.I18nUtils;
import com.pro.common.modules.service.dependencies.util.IPUtils;
import com.pro.common.web.security.service.CommonLoginService;
import com.pro.common.web.security.service.TokenService;
import com.pro.framework.api.cache.ICacheManagerCenter;
import com.pro.framework.api.util.AssertUtil;
import com.pro.framework.api.util.StrUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Api(tags = "公共登录")
@RestController
@RequestMapping("/commonLogin")
public class CommonLoginController {

    @Autowired
    private CommonLoginService commonLoginService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CommonProperties commonProperties;
    @Autowired
    private IAuthRoleService authRoleService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ICacheManagerCenter cacheManagerCenter;

    @ApiOperation("注册")
    @PostMapping("/register")
    public R<String> register(HttpServletRequest request, HttpServletResponse response, @RequestBody String requestBody) {
        // 图形验证码
        JSONObject jo = JSONUtil.parseObj(requestBody);
        if (EnumDict.REGISTER_PROPS.getValueCache().contains(",captcha,")) {
            String captcha = jo.getStr("captcha");
            String code = (String) request.getSession().getAttribute(CommonConst.Str.SYSTEM_CAPTCHA);
            if (StrUtils.isBlank(captcha)) {
                throw new BusinessException("图形验证码不正确");
            } else if (!captcha.equals(code)) {
                throw new BusinessException("图形验证码不正确");
            }
        }
        if (EnumDict.REGISTER_PROPS.getValueCache().contains(",smsCode,")) {
            String codeInput = jo.getStr("smsCode");
            String key = userService.getMsgKey(jo.toBean(User.class), EnumSysMsgBusinessCode.REGISTER_CODE.name());
            String code = (String) cacheManagerCenter.get(CommonConst.CacheKey.SmsCode, key);
            AssertUtil.isTrue(StrUtil.isNotBlank(code) && code.equals(codeInput),"短信或邮箱验证码不正确");
        }


        String ip = IPUtils.getIpAddress(request);
        // 返回token
        String token = commonLoginService.register(requestBody, ip, LocaleContextHolder.getLocale().toLanguageTag());
        response.setHeader("Authorization", token);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        return R.ok(token);
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    public R<String> login(HttpServletResponse response, @RequestBody String request) {
        // 返回token
        String token = commonLoginService.login(request);
        response.setHeader("Authorization", token);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        return R.ok(token);
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public R<?> logout(ILoginInfo loginInfo, @RequestBody(required = false) LoginRequest request) {
        Long id = loginInfo.getId();
        if (id != null) {
            // 移除token
            tokenService.evict(request.getToken());
        }
        return R.ok();
    }

    @ApiOperation("查询登录信息")
    @GetMapping("/getLoginInfo")
    public R<ILoginInfo> getLoginInfo(ILoginInfo loginInfo) {
        return R.ok(commonLoginService.getLoginInfo(loginInfo.getId()));
    }

    @ApiOperation("查询是否开启谷歌秘钥登录")
    @GetMapping("/getGoogleAuthOpen")
    public R<Boolean> getGoogleAuthOpen(LoginRequest loginRequest) {
        return R.ok(commonLoginService.getGoogleAuthOpen(loginRequest));
    }


    @ApiOperation(value = "检查是否登录")
    @GetMapping("/validate")
    public R<Boolean> validateToken(HttpServletRequest request) {
        return R.ok(null != tokenService.getIdByToken(request.getHeader(TokenService.tokenKey)));
    }

    @ApiOperation(value = "更新token")
    @PostMapping("/refreshToken")
    public R<String> refreshToken(HttpServletRequest request) {
        // 返回token
        String token = tokenService.refresh(request.getHeader(TokenService.tokenKey));
        return R.ok(token);
    }

    @ApiOperation(value = "获取权限")
    @GetMapping("/getAuthRouteCodes")
    public R<Set<String>> getAuthRouteCodes(HttpServletRequest request) {
        EnumSysRole role = commonProperties.getApplication().getRole();
        Long loginId = tokenService.getIdByToken(request.getHeader(TokenService.tokenKey));
        Set<String> routeCodes = authRoleService.getRouteCodes(role, loginId);
        return R.ok(routeCodes);
    }
}
