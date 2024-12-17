package com.pro.common.module.service.common.service;

import com.pro.common.module.service.common.dao.SysMultiClassRelationDao;
import com.pro.common.module.api.common.model.db.SysMultiClassRelation;
import com.pro.framework.api.model.IModel;
import com.pro.framework.api.util.ClassUtils;
import com.pro.framework.mtq.service.multiwrapper.entity.IMultiClassRelationService;
import com.pro.framework.mtq.service.multiwrapper.util.MultiClassRelationFactory;
import com.pro.framework.mybatisplus.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * ƒ
 * 服务实现类
 */
@Service
@Slf4j
public class SysMultiClassRelationService extends BaseService<SysMultiClassRelationDao, SysMultiClassRelation> implements IMultiClassRelationService<SysMultiClassRelation> {

    @Override
    public List<SysMultiClassRelation> loadRelation() {
        return this.list();
    }

    @Override
    public Collection<Class<?>> loadClasses() {
        return ClassUtils.scanPackageBySuper("com.pro", IModel.class);
    }

    @Override
    public Class<?> getClass(String entityClassName) {
        return MultiClassRelationFactory.INSTANCE.getEntityClass(entityClassName);
    }
}
