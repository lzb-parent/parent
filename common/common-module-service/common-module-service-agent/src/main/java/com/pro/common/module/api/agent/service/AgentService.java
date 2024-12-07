package com.pro.common.module.api.agent.service;

import com.pro.common.module.api.agent.dao.AgentDao;
import com.pro.common.module.api.agent.intf.IAgentService;
import com.pro.common.module.api.agent.model.db.Agent;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import com.pro.common.modules.api.dependencies.model.LoginRequest;
import com.pro.common.modules.api.dependencies.service.ILoginInfoService;
import com.pro.common.modules.api.dependencies.user.intf.InviteCodeService;
import com.pro.common.modules.service.dependencies.util.I18nUtils;
import com.pro.framework.api.util.StrUtils;
import com.pro.framework.mybatisplus.BaseService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service(CommonConst.Bean.agentService)
public class AgentService extends BaseService<AgentDao, Agent> implements IAgentService<Agent>, InviteCodeService, ILoginInfoService<Agent> {

    @Override
    public List<Long> getAllChildIdList(Long agentId) {
        return baseMapper.getAllChildIdList(agentId);
    }

    @Override
    public ILoginInfo getByCode(String code) {
        return this.lambdaQuery().eq(Agent::getCodeNumber, code).last("limit 1").one();
    }


    @Override
    public boolean save(Agent entity) {
        this.doBefore(entity);
        return super.save(entity);
    }

    @Override
    public boolean saveOrUpdate(Agent entity) {
        this.doBefore(entity);
        return super.saveOrUpdate(entity);
    }

    //    @Override
    public void doBefore(Agent entity) {
        int level;
        Long pid = entity.getPid();
        String pids;
        if (pid == null) {
            level = 1;
            pids = "";
        } else {
            Agent parent = this.getById(pid);
            level = parent.getLevel() + 1;
            Long ppid = parent.getPid();
            pids = (null == ppid ? "" : ppid) + "," + pid;
        }
        entity.setLevel(level);
        entity.setPids(pids);
        // 新增生成推荐码
        if (null == entity.getId()) {
            String finalCode = entity.getCodeNumber();
            if (null == finalCode) {
                throw new BusinessException("请输入邀请码");
            } else {
                if (AgentOtherService.inviteCodeServices.stream().anyMatch(service -> null != service.getByCode(finalCode))) { // 保证唯一性
                    throw new BusinessException("邀请码_已被使用", finalCode);
                }
            }
        } else {
            entity.setCodeNumber(null);
        }
    }

    @Override
    public Agent doLogin(LoginRequest loginRequest) {
        return this.lambdaQuery().eq(Agent::getUsername, loginRequest.getUsername()).one();
    }

    @Override
    public Set<Long> getRoleIds(Long loginId) {
        return Arrays.stream(this.getById(loginId).getRoleIds().split(",")).filter(StrUtils::isNotBlank).map(Long::valueOf).collect(Collectors.toSet());
    }

    @Override
    public Agent getById(Serializable id) {
        return super.getById(id);
    }
}
