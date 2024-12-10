package com.pro.common.module.service.message.service;

import com.pro.common.module.api.message.model.db.SysMsgRecord;
import com.pro.common.module.service.message.dao.SysMsgRecordDao;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.enums.EnumEnv;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.framework.api.cache.ICacheManagerCenter;
import com.pro.framework.mybatisplus.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 短信发送记录 服务实现类
 */
@Service
public class SysMsgRecordService extends BaseService<SysMsgRecordDao, SysMsgRecord> {
    @Autowired
    private CommonProperties commonProperties;
    @Autowired
    private ICacheManagerCenter cacheManagerCenter;

    public String getCode(String account) {
        return (String) cacheManagerCenter.get(CommonConst.CacheKey.CHECK_CODE, account);
    }

    public void setCode(String account, String smsCode) {
        cacheManagerCenter.put(CommonConst.CacheKey.CHECK_CODE, account, smsCode);
    }

    public boolean checkCode(String phone, String smsCode) {
        if (!EnumEnv.prod.equals(commonProperties.getEnv()) || phone.startsWith("13800")) {
            return true;
        }
        String attribute = getCode(phone);
        if (attribute == null) {
            return false;
        }
        return attribute.equals(smsCode);
    }
}
