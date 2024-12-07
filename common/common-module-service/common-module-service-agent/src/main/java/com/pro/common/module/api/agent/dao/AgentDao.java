package com.pro.common.module.api.agent.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pro.common.module.api.agent.model.db.Agent;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AgentDao extends BaseMapper<Agent> {
    @Select("select id from agent where find_in_set(#{agentId}, pids) and enabled = 1 and deleted = 0")
    List<Long> getAllChildIdList(Long agentId);
}
