package com.pro.common.module.api.system.model.util;

import com.pro.common.module.api.system.model.enums.IEnumAuthDict;
import com.pro.common.module.api.system.model.intf.IAuthDictService;

//@Component
public class AuthDictUtil {
    /**
     * 启动时需要初始化
     */
    public static void init(IAuthDictService authDictService) {
        AuthDictUtil.authDictService = authDictService;
    }

    public static IAuthDictService authDictService;

    //    @Autowired
    public static String getValueCache(IEnumAuthDict enumDict) {
        return authDictService.getValueCache(enumDict);
    }
    public static String getValue(IEnumAuthDict enumDict) {
        return authDictService.getValue(enumDict);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValueCacheOrDefault(IEnumAuthDict enumDict, T t) {
        return authDictService.getValueCacheOrDefault(enumDict, t);
    }
}
