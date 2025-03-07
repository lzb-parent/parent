package com.pro.common.modules.api.dependencies.auth;

import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import io.swagger.v3.oas.annotations.Parameter;
import com.pro.framework.api.model.IModel;

import java.util.List;
import java.util.Map;

public interface IAgentUserFilterService {
    /**
     * User.pids上下级关系过滤用户信息
     */
    void filterUserTeamQuery(@Parameter(hidden = true) ILoginInfo loginInfo, Map<String, Object> paramMap, UserDataQuery query, String userIdPropName, Class<?> entityClass);

    /**
     * User.agentId  Agent.pids 上下级关系过滤用户信息
     */
    void filterAgentQuery(Map<String, Object> paramMap, Long loginAgentId, UserDataQuery query);

    <T extends IModel> void filterAgentInsertUpdate(@Parameter(hidden = true) ILoginInfo loginInfo, List<T> records);

    List<Long> getAgentUserIds(Long loginAgentId, Boolean isChildAll, Long agentId, Long userId);
}
