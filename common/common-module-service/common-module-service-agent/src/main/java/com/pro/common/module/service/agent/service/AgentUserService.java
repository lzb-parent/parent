package com.pro.common.module.service.agent.service;


import cn.hutool.core.util.ObjUtil;
import com.pro.common.module.api.agent.intf.IAgentService;
import com.pro.common.module.api.agent.model.db.Agent;
import com.pro.common.module.api.system.model.enums.EnumAuthDict;
import com.pro.common.module.api.user.intf.IUserService;
import com.pro.common.modules.api.dependencies.auth.IAgentUserFilterService;
import com.pro.common.modules.api.dependencies.auth.UserDataQuery;
import com.pro.common.modules.api.dependencies.message.ISysMsgExtendInfoService;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import io.swagger.v3.oas.annotations.Parameter;
import com.pro.common.modules.api.dependencies.model.classes.IUserClass;
import com.pro.common.modules.api.dependencies.user.model.IUserMsg;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.framework.api.model.IModel;
import com.pro.framework.api.structure.TriConsumer;
import com.pro.framework.api.util.AssertUtil;
import com.pro.framework.api.util.LogicUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Service
public class AgentUserService implements IAgentUserFilterService, ISysMsgExtendInfoService {
    @Autowired
    private CommonProperties commonProperties;
    //    @Autowired
//    private IEnumProperties enumProperties;
    @Autowired(required = false)
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
                if (agentService != null) {
                    Collection<Long> agentIdSubs = agentService.getAllChildIdList(loginAgentId);
                    AssertUtil.isTrue(agentIdSubs.contains(agentId), "暂无权限");
                }
            }
        }
        Long agentIdTarget = ObjUtil.defaultIfNull(agentId, loginAgentId);
        agentIdsTarget.add(agentIdTarget);
        // 查询所有下级,在过滤看自己的数据,意义
        if (isChildAll && null != agentService) {
            agentIdsTarget.addAll(agentService.getAllChildIdList(agentIdTarget));
        }
        return userService.getIdsByAgentIds(agentIdsTarget);
    }

    @Override
    public void filterAgentQuery(Map<String, Object> paramMap, Long loginAgentId, UserDataQuery query) {
        setUser(paramMap, loginAgentId, query);
    }

    @Override
    public <T extends IModel> void filterAgentInsertUpdate(@Parameter(hidden = true) ILoginInfo loginInfo, List<T> records) {
        List<Long> userIds = this.getAgentUserIds(loginInfo.getId(), true, null, null);
        records.forEach(record -> AssertUtil.isTrue(userIds.contains(((IUserClass) record).getUserId()), "暂无权限"));
    }

    @Override
    public void filterUserTeamQuery(@Parameter(hidden = true) ILoginInfo loginInfo, Map<String, Object> paramMap, UserDataQuery query, String userIdPropName, Class<?> entityClass) {
        Long userId = loginInfo.getId();
        // 是否查询用户下级的信息
        Boolean userTeamFlag = query.getUserTeamFlag();
        if (null != userTeamFlag && userTeamFlag) {
            // 查询下级信息
            List<Long> userIdAll = new ArrayList<>();
            List<Long> userIds = Collections.singletonList(userId);
            for (int i = 0; i < EnumAuthDict.USER_TEAM_LEVELS.getValueCacheOrDefault(3); i++) {
                userIds = userService.listIdByPids(userIds);
                userIdAll.addAll(userIds);
            }
            if (userIdAll.isEmpty()) {
                paramMap.put(userIdPropName, "-10000");
            } else {
                paramMap.put(userIdPropName, "#in#" + userIdAll.stream().map(Objects::toString).collect(Collectors.joining(",")));
            }
        } else {
            // 只能查询自己的
            paramMap.put(userIdPropName, userId);
        }
    }

    @Override
    public void setExtendInfo(Map<Long, IUserMsg> userMap, TriConsumer<Long, Long, String> consumer) {
        Map<Long, Agent> agentMap = null == agentService ? Collections.emptyMap() : agentService.idMap(userMap.values().stream().map(IUserMsg::getAgentId).collect(Collectors.toList()));
        userMap.values().forEach(u -> {
            Agent agent = agentMap.get(u.getAgentId());
            consumer.accept(u.getId(), agent.getId(), agent.getUsername());
        });
    }
}
