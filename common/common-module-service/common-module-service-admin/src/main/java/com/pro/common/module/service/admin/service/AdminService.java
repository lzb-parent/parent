package com.pro.common.module.service.admin.service;

import com.pro.common.module.service.admin.dao.AdminDao;
import com.pro.common.module.service.admin.model.db.Admin;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.model.LoginRequest;
import com.pro.common.modules.api.dependencies.service.ILoginInfoService;
import com.pro.framework.api.util.AssertUtil;
import com.pro.framework.api.util.PasswordUtils;
import com.pro.framework.api.util.StrUtils;
import com.pro.framework.mybatisplus.BaseService;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component(CommonConst.Bean.adminService)
public class AdminService extends BaseService<AdminDao, Admin> implements ILoginInfoService<Admin> {
    @Override
    public Admin getLoginInfo(LoginRequest loginRequest) {
        return this.lambdaQuery().eq(Admin::getUsername, loginRequest.getUsername()).one();
    }

    @Override
    public Admin getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public Set<Long> getRoleIds(Long loginId) {
        return Arrays.stream(this.getById(loginId).getRoleIds().split(",")).filter(StrUtils::isNotBlank).map(Long::valueOf).collect(Collectors.toSet());
    }

    @Override
    public boolean updateById(Admin entity) {
//        @Parameter(hidden = true) ILoginInfo loginInfo = ThreadLocalUtil.getLoginInfo();
//        // 修改自己的密码
//        if (loginInfo.getId().equals(entity.getId())) {
//            if (StrUtils.isNotBlank(entity.getPassword())) {
//                this.checkOldPassword(entity, Admin::getPassword);
//                entity.setPassword(PasswordUtils.encrypt_Password(entity.getPassword()));
//            } else {
//                throw new BusinessException("请在个人中心修改自己的密码");
//            }
//        }
//        // 有编辑权限
//        else if (loginInfo.getPaths().contains("/sys/admin")) {
//        }
        entity.setPassword(PasswordUtils.encrypt_Password(entity.getPassword()));
        return super.updateById(entity);
    }

    public void checkOldPassword(Admin entity, Function<Admin, String> passwordFun) {
        Admin entityOld = this.getById(entity.getId());
        if (entityOld == null) {
            return;
        }
        String passwordOld = passwordFun.apply(entityOld);
        AssertUtil.isTrue(StrUtils.isBlank(passwordOld) || passwordOld.equals(PasswordUtils.encrypt_Password(entity.getPasswordOld())), "请输入正确的旧密码");
    }
}
