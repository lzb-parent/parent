package com.pro.common.module.api.agent.intf;

import com.pro.common.module.api.agent.model.db.Agent;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IAgentService<T extends Agent> {
    Collection<Long> getAllChildIdList(Long agentId);

    Map<Long, T> idMap(Collection<? extends Serializable> ids);
}
