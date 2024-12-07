package com.pro.common.module.service.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pro.common.module.api.pay.model.db.UserTransfer;

/**
 * 转账记录 Mapper 接口
 */
public interface UserTransferDao extends BaseMapper<UserTransfer> {

//    BillCountData countData(
//            @Param("agentIdList") List<Long> agentIdList,
//            @Param("userIdList") List<Long> userIdList,
//            @Param("start") String start,
//            @Param("end") String end,
//            @Param("accountTypes") List<EnumAccountType> accountTypes);
}
