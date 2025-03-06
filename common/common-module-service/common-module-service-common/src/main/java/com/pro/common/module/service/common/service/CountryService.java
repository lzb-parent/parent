package com.pro.common.module.service.common.service;

import com.pro.common.module.service.common.dao.CountryDao;
import com.pro.common.module.api.common.model.db.Country;
import com.pro.common.modules.api.dependencies.common.service.ICountryService;
import com.pro.framework.mybatisplus.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 国家 服务实现类
 */
@Service
public class CountryService extends BaseService<CountryDao, Country> implements ICountryService {

    public List<Country> getActiveList() {
        return list(qw().lambda().eq(Country::getEnabled, true).orderByAsc(Country::getSort));
    }
    @Override
    public Country getActiveFirst() {
        return getOne(qw().lambda().eq(Country::getEnabled, true).orderByAsc(Country::getSort).last("limit 1"));
    }
}
