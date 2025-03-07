package com.pro.common.web.security.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.pro.common.module.api.message.intf.ISysMsgService;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.R;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import com.pro.common.modules.api.dependencies.model.TokenRequest;
import io.swagger.v3.oas.annotations.Parameter;
import com.pro.common.modules.api.dependencies.model.ILoginInfoPrepare;
import com.pro.common.modules.api.dependencies.model.LoginRequest;
import com.pro.common.modules.api.dependencies.service.IAuthRoleService;
import com.pro.common.modules.service.dependencies.modelauth.base.AccessToken;
import com.pro.common.modules.service.dependencies.modelauth.base.TokenService;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.common.web.security.service.CommonLoginService;
import com.pro.framework.api.cache.ICacheManagerCenter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@Tag(name = "统一登录注册接口")
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

    @ApiOperationSupport(order = 1)
    @Operation(summary = "注册")
    @PostMapping("/register")
    public R<AccessToken> register(HttpServletRequest request, HttpServletResponse response, @Parameter(description = "用户基础信息") @RequestBody String requestBody) {
        String captchaInner = (String) request.getSession().getAttribute(CommonConst.Str.SYSTEM_CAPTCHA);
        AccessToken token = commonLoginService.register(request, captchaInner, requestBody);
        response.setHeader("Authorization", token.getAccessToken());
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        return R.ok(token);
    }

    @ApiOperationSupport(order = 2)
    @Operation(summary = "登录")
    @PostMapping("/login")
    public R<AccessToken> login(HttpServletResponse response, @RequestBody LoginRequest request) {
        // 返回token
        AccessToken token = commonLoginService.login(request);
        response.setHeader("Authorization", token.getAccessToken());
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        return R.ok(token);
    }

    @ApiOperationSupport(order = 3)
    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public R<?> logout(@Parameter(hidden = true) ILoginInfo loginInfo, @Parameter(required = true) TokenRequest request) {
        Long id = loginInfo.getId();
        if (id != null) {
            // 移除token
            tokenService.evict(request.getToken());
        }
        return R.ok();
    }

    @ApiOperationSupport(order = 4)
    @Operation(summary = "查询登录信息")
    @GetMapping("/getLoginInfo")
    public R<ILoginInfoPrepare> getLoginInfo(@Parameter(hidden = true) ILoginInfo loginInfo) {
        return R.ok(commonLoginService.getLoginInfo(loginInfo.getId()));
    }

    @ApiOperationSupport(order = 5)
    @Operation(summary = "查询是否开启谷歌秘钥登录")
    @GetMapping("/getGoogleAuthOpen")
    public R<Boolean> getGoogleAuthOpen(LoginRequest loginRequest) {
        return R.ok(commonLoginService.getGoogleAuthOpen(loginRequest));
    }


    @ApiOperationSupport(order = 6)
    @Operation(summary = "检查是否登录")
    @GetMapping("/validate")
    public R<Boolean> validateToken(HttpServletRequest request) {
        return R.ok(null != tokenService.getIdByToken(request.getHeader(TokenService.tokenKey)));
    }

    @ApiOperationSupport(order = 7)
    @Operation(summary = "更新token")
    @PostMapping("/refreshToken")
    public R<AccessToken> refreshToken(HttpServletRequest request) {
        // 返回token
        AccessToken token = tokenService.refresh(request.getHeader(TokenService.tokenKey));
        return R.ok(token);
    }

    @ApiOperationSupport(order = 8)
    @Operation(summary = "获取权限")
    @GetMapping("/getAuthRouteCodes")
    public R<Set<String>> getAuthRouteCodes(HttpServletRequest request) {
        EnumSysRole role = commonProperties.getApplication().getRole();
        Long loginId = tokenService.getIdByToken(request.getHeader(TokenService.tokenKey));
        Set<String> routeCodes = authRoleService.getRouteCodes(role, loginId);
        return R.ok(routeCodes);
    }
}
