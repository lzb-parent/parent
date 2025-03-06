package com.pro.common.modules.service.dependencies.modelauth.base;

import com.pro.common.module.api.system.model.enums.EnumAuthDict;
import com.pro.common.module.api.system.model.intf.IAuthDictService;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.enums.EnumApplication;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.common.modules.api.dependencies.model.ILoginInfoPrepare;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.framework.api.FrameworkConst;
import com.pro.framework.api.cache.ICacheManagerCenter;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

/**
 * 用户登录token存取服务
 */
public class TokenService {
    public TokenService(CommonProperties commonProperties, ICacheManagerCenter cacheManagerRemote, IAuthDictService authDictService) {
        this.commonProperties = commonProperties;
        this.cacheManagerRemote = cacheManagerRemote;
        TokenService.tokenKey = authDictService.getValueOnStart(EnumAuthDict.SYSTEM_VERSION, 0) + "-" + commonProperties.getApplication().name().toUpperCase();
    }

    /**
     * 中央缓存服务器(远程)
     */
//    @Autowired
    private CommonProperties commonProperties;
    /**
     * 中央缓存服务器(远程)
     */
//    @Autowired
    private ICacheManagerCenter cacheManagerRemote;

    public static String tokenKey;

    /**
     * 生成token
     */
    public AccessToken generate(ILoginInfoPrepare loginInfo) {
        return this.generate(loginInfo.getSysRole().toString(), loginInfo.getId().toString());
    }

    /**
     * 删除token
     */
    public String[] evict(String tokenOld) {
        String[] props = tokenOld.split("_");
        cacheManagerRemote.evict(getLoginCacheName(props[0], props[1]), tokenOld);
        return props;
    }


    /**
     * 根据token查询用户Id
     */
    public Long getIdByToken(String tokenOld) {
        if (tokenOld == null || tokenOld.isEmpty() || "null".equals(tokenOld) || "undefined".equals(tokenOld)) {
            return null;
        }
        EnumSysRole role = commonProperties.getApplication().getRole();
        String[] props = tokenOld.split("_");
        String id = (String) cacheManagerRemote.get(getLoginCacheName(null == role ? props[0] : role.toString(), props[1]), tokenOld);
        if (id == null) {
            return null;
        }
        return Long.valueOf(id);
    }

    /**
     * 刷新token
     */
    public AccessToken refresh(String tokenOld) {
        String[] props = tokenOld.split("_");
        // 删除旧token
        this.evict(tokenOld);
        // 添加token
        return this.generate(props[0], props[1]);
    }


    /**
     * 批量删除token
     */
    public void evictBatch(EnumApplication application, Long id) {
        cacheManagerRemote.clearCache(getLoginCacheName(application.name(), id.toString())); // props[0], props[1]
    }

    private AccessToken generate(String role, String id) {
        String token = String.join(FrameworkConst.Str.UNDERLINE, role, id, generateToken(TOKEN_LENGTH) + System.currentTimeMillis());
        Integer expiresInSecond = commonProperties.getRoleTokenValidSecondMap().getOrDefault(role, 3600);
        cacheManagerRemote.put(getLoginCacheName(role, id), token, id, expiresInSecond, TimeUnit.SECONDS);
        return new AccessToken(token, expiresInSecond, "Bearer");
    }

    // 定义字符集，包含数字、大写字母和小写字母
    private static final int TOKEN_LENGTH = 30;
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateToken(int length) {
        String characters = CommonConst.Str.CHARACTERS;
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < length; i++) {
            token.append(characters.charAt(secureRandom.nextInt(characters.length())));
        }
        return token.toString();
    }

    private static String getLoginCacheName(String application, String id) {
        return String.join(ICacheManagerCenter.SPLIT, application, id);
    }


}
