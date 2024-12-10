package com.pro.common.module.api.user.service;

import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.pro.common.module.api.user.dao.UserBankCardDao;
import com.pro.common.module.api.user.intf.IUserBankCardService;
import com.pro.common.module.api.user.intf.IUserService;
import com.pro.common.module.api.user.model.db.User;
import com.pro.common.module.api.user.model.db.UserBankCard;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.framework.api.util.AssertUtil;
import com.pro.framework.api.util.LambdaUtil;
import com.pro.framework.api.util.PasswordUtils;
import com.pro.framework.api.util.inner.FunSerializable;
import com.pro.framework.mybatisplus.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 代付渠道 服务实现类
 */
@Service
public class UserBankCardService extends BaseService<UserBankCardDao, UserBankCard> implements IUserBankCardService {
    @Autowired
     private CommonProperties commonProperties;
    @Autowired
     private IUserService userService;

    public List<UserBankCard> getActiveList() {
        return this.lambdaQuery().orderByAsc(UserBankCard::getSort).list();
    }

    /**
     * 更新属性
     *
     * @return
     */
    public boolean updates(UserBankCard param, UserBankCard setData, FunSerializable<UserBankCard, ?>... functions) {
        UpdateChainWrapper<UserBankCard> wrapper = this.update();
        wrapper.setEntity(param);
//        LambdaUpdateChainWrapper<UserBankCard> wrapper = this.lambdaUpdate().setEntity(param);
        for (FunSerializable<UserBankCard, ?> function : functions) {
            wrapper.set(LambdaUtil.resolveCache(function).getPropName(), function.apply(setData));
        }
       return wrapper.update();
    }

    @Override
    public UserBankCard getById(Long id) {
        return super.getById(id);
    }

    @Override
    public boolean save(UserBankCard entity) {
        this.saveOrUpdateBeforeCheck(entity);
        return super.save(entity);
    }


    @Override
    public boolean updateById(UserBankCard entity) {
        this.saveOrUpdateBeforeCheck(entity);
        return super.updateById(entity);
    }

    @Override
    public boolean saveOrUpdate(UserBankCard entity) {
        this.saveOrUpdateBeforeCheck(entity);
        return super.saveOrUpdate(entity);
    }

    private void saveOrUpdateBeforeCheck(UserBankCard entity) {
        User user = null;
        switch (commonProperties.getApplication()){
            case user:
                user = userService.getById(entity.getUserId());
                AssertUtil.isTrue(user.getTkPassword().equals(PasswordUtils.encrypt_tkPassword(entity.getTkPassword())), "提款密码错误");
                break;
        }
        if (entity.getId() == null) {
            if (user == null) {
                user = userService.getById(entity.getUserId());
            }
            entity.setUsername(user.getUsername());
            entity.setIsDemo(user.getIsDemo());
        }
    }
}

