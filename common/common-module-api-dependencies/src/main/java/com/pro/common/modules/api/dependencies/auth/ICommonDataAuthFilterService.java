package com.pro.common.modules.api.dependencies.auth;

import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import com.pro.framework.api.model.IModel;

import java.util.List;
import java.util.Map;

public interface ICommonDataAuthFilterService {
    void filterQuery(Map<String, Object> paramMap, Long loginAgentId, UserDataQuery query);

    <T extends IModel> void filterInsertUpdate(ILoginInfo loginInfo, List<T> records);

    void filterQueryUserTeam(ILoginInfo loginInfo, Map<String, Object> paramMap, UserDataQuery query, String userIdPropName);

    List<Long> getAgentUserIds(Long loginAgentId, Boolean isChildAll, Long agentId, Long userId);
}
