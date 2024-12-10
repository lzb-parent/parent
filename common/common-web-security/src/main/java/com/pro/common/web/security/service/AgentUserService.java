package com.pro.common.web.security.service;


import cn.hutool.core.util.ObjUtil;
import com.pro.common.module.api.agent.intf.IAgentService;
import com.pro.common.module.api.user.intf.IUserService;
import com.pro.common.modules.service.dependencies.modelauth.base.UserDataQuery;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.framework.api.util.AssertUtil;
import com.pro.framework.api.util.LogicUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Service
public class AgentUserService {
    @Autowired
    private CommonProperties commonProperties;
//    @Autowired
//    private IEnumProperties enumProperties;
    @Autowired
    private IUserService userService;
    @Autowired(required = false)
    private IAgentService agentService;
    Pattern pattern = Pattern.compile("^#..#.*$");

    public void setUser(Map<String, Object> paramMap, Long currentLoginAgentId, UserDataQuery query) {
        switch (commonProperties.getApplication()) {
            case user:
                return;
            case agent:
                break;
            case admin:
                currentLoginAgentId = null;
                break;
        }
        String firstUserIdProp = "userId";
        Object userId = paramMap.get(firstUserIdProp);
        if (userId != null && pattern.matcher(userId.toString()).matches()) {
            return;
        }
        // 过滤代理+userIds
        String userIdAppend = this.getUserIdAppend(currentLoginAgentId, query, userId);
        if (userIdAppend != null) {
            paramMap.put(firstUserIdProp, userIdAppend);
        }
    }


    /**
     * 过滤代理+userIds
     */
    private String getUserIdAppend(Long loginAgentId, UserDataQuery query, Object userId) {
        List<Long> userIds = this.getAgentUserIds(loginAgentId, query.getIsChildAll(), query.getAgentId(), (null == userId || "".equals(userId)) ? null : Long.valueOf(userId.toString()));
        // 过滤代理下的用户
        String userIdAppend = userIds.stream().map(Objects::toString).collect(Collectors.joining(","));
        return userIdAppend.isEmpty() ? null : ("#in#" + userIdAppend);
    }

    public List<Long> getAgentUserIds(Long loginAgentId, Boolean isChildAll, Long agentId, Long userId) {
        isChildAll = ObjUtil.defaultIfNull(isChildAll, false);
        // 批量获取代理用户id集合
        List<Long> userIdList = this.getIdsByAgentId(loginAgentId, agentId, isChildAll);
        if (userId != null) {
            // 取交集userId
            userIdList = userIdList.contains(userId) ? Collections.singletonList(userId) : Collections.emptyList();
        }
        return userIdList;
    }

    private List<Long> getIdsByAgentId(Long loginAgentId, Long agentId, Boolean isChildAll) {
        loginAgentId = LogicUtils.or(loginAgentId, agentId);
        Collection<Long> agentIdsTarget = new ArrayList<>();
        if (agentId != null && !loginAgentId.equals(agentId)) {
            if (agentId == 0) {
                agentId = loginAgentId;
            } else {
                Collection<Long> agentIdSubs = agentService.getAllChildIdList(loginAgentId);
                AssertUtil.isTrue(agentIdSubs.contains(agentId), "permission error");
            }
        }
        Long agentIdTarget = ObjUtil.defaultIfNull(agentId, loginAgentId);
        agentIdsTarget.add(agentIdTarget);
        // 查询所有下级,在过滤看自己的数据,意义
        if (isChildAll) {
            agentIdsTarget.addAll(agentService.getAllChildIdList(agentIdTarget));
        }
        return userService.getIdsByAgentIds(agentIdsTarget);
    }
}
