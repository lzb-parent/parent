package com.pro.common.module.service.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pro.common.module.api.pay.model.db.PayChannel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 支付渠道 Mapper 接口
 */
public interface PayChannelDao extends BaseMapper<PayChannel> {

    List<PayChannel> getActiveList();

    IPage<PayChannel> getList(Page page, @Param("vo") PayChannel payChannel);

}
