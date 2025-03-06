package com.pro.common.module.service.user.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.pro.common.module.api.message.enums.EnumSysMsgBusinessCode;
import com.pro.common.module.api.message.intf.ISysMsgService;
import com.pro.common.module.api.system.model.enums.EnumAuthDict;
import com.pro.common.module.api.user.enums.EnumRegisterUsernameFrom;
import com.pro.common.module.api.user.intf.IUserService;
import com.pro.common.module.api.user.model.db.User;
import com.pro.common.module.service.user.dao.UserDao;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.R;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.api.dependencies.model.ILoginInfoPrepare;
import com.pro.common.modules.api.dependencies.model.LoginRequest;
import com.pro.common.modules.api.dependencies.service.ILoginInfoService;
import com.pro.common.modules.api.dependencies.user.intf.IUserRegisterInitService;
import com.pro.common.modules.api.dependencies.user.intf.InviteCodeService;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.common.modules.service.dependencies.util.TransactionUtil;
import com.pro.framework.api.FrameworkConst;
import com.pro.framework.api.database.AggregateResult;
import com.pro.framework.api.database.GroupBy;
import com.pro.framework.api.database.TimeQuery;
import com.pro.framework.api.database.page.IPageInput;
import com.pro.framework.api.util.AssertUtil;
import com.pro.framework.api.util.JSONUtils;
import com.pro.framework.api.util.PasswordUtils;
import com.pro.framework.api.util.StrUtils;
import com.pro.framework.mtq.service.multiwrapper.dto.MultiPageResult;
import com.pro.framework.mtq.service.multiwrapper.entity.IMultiPageResult;
import com.pro.framework.mybatisplus.BaseService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

//@Service(CommonConst.Bean.userService)
@Slf4j
public class UserService<M extends UserDao<T>, T extends User> extends BaseService<M, T> implements ILoginInfoService<T>, IUserService<T>, InviteCodeService {
    @Autowired
    private CommonProperties commonProperties;
    @Autowired
    private ISysMsgService messageService;
    @Autowired
    private List<IUserRegisterInitService> registerInitServices;


    @Override
    public T doLogin(LoginRequest loginRequest) {
        T user = this.query(loginRequest);
        if (user != null) {
            this.addIncreaseField(user.getId(), T::getLoginTimes, 1);
        }
        return user;
    }

    @Override
    public  T query(LoginRequest loginRequest) {
        if (StrUtils.isNotBlank(loginRequest.getUsername())) {
            return this.lambdaQuery().eq(T::getUsername, loginRequest.getUsername()).one();
        }
        if (StrUtils.isNotBlank(loginRequest.getPhone())) {
            return this.lambdaQuery().eq(T::getPhone, loginRequest.getPhone()).one();
        }
        if (StrUtils.isNotBlank(loginRequest.getEmail())) {
            return this.lambdaQuery().eq(T::getEmail, loginRequest.getEmail()).one();
        }
        return null;
    }

    @Override
    public T register(String request, String ip, String lang) {
        T user = JSONUtils.fromString(request, entityClass);
        String username = user.getUsername();
        EnumRegisterUsernameFrom usernameFrom = EnumRegisterUsernameFrom.MAP.getOrDefault(EnumAuthDict.REGISTER_USERNAME_FROM.getValueCache(), EnumRegisterUsernameFrom.INPUT);
        switch (usernameFrom) {
            case INPUT:
                break;
            case PHONE:
                username = user.getPhone();
                break;
            case EMAIL:
                username = user.getEmail();
                break;
            default:
                throw new BusinessException("config usernameFrom error");
        }
        user.setUsername(username);
        if (this.lambdaQuery().eq(T::getUsername, username).count() > 0) {
            throw new BusinessException("该用户名已经被注册了", username);
        }
        user.setEnabled(true);
        user.setIsDemo(false);

        user.setRegisterIp(ip);

        this.save(user);

        TransactionUtil.doAfter(() -> {
            messageService.sendMsgAllChannelType(user, EnumSysMsgBusinessCode.REGISTER_SUCCESS.name(), Collections.singletonMap("user", user), lang, ip, false, false, null);
        });
        return user;
    }

    /**
     * 推荐码
     */
    public String buildCode(String letters) {
        String split = StrUtils.EMPTY;
        //随机取6位数
        String code = RandomUtil.randomString(letters, 6);
        List<String> ss = Arrays.stream(code.split(split)).collect(Collectors.toList());
        // 顺序打乱
        Collections.shuffle(ss);
        code = String.join(split, ss).toUpperCase();
        String finalCode = code;
        if (UserOtherService.inviteCodeServices.stream().allMatch(service -> null == service.getByCode(finalCode))) { // 保证唯一性
            // 没重复,有效推广码
            return finalCode;
        }
        // 重复,重新生成推广码
        return this.buildCode(letters);
    }

    @Override
    public List<Long> getIdsByAgentIds(Collection<Long> agentIds) {
        return this.lambdaQuery()
                .select(T::getId)
                .in(T::getAgentId, agentIds)
                .list().stream().map(T::getId).collect(Collectors.toList());
    }

//    @Override
//    public T getById(Long userId) {
//        return super.getById(userId);
//    }

    @Override
    public T getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public Set<Long> getRoleIds(Long loginId) {
        return Collections.emptySet();
    }

    @Override
    @SneakyThrows
    public T newInstant() {
        //noinspection deprecation
        return entityClass.newInstance();
    }

    // 更新
    @Override
    public boolean saveOrUpdate(T entity) {
        this.doSaveOrUpdate(entity);
        return super.saveOrUpdate(entity);
    }

    // 保存
    @Override
    public boolean save(T entity) {
        // 个人推荐码
        entity.setCode(this.buildCode(EnumAuthDict.REGISTER_CODE_LETTERS_USER.getValueCache()));
        // 上级推荐码
        this.loadInviteCode(entity);
        // 其他保存
        this.doSaveOrUpdate(entity);
        boolean save = super.save(entity);
        if (save) {
            // 其他注册初始化内容
            registerInitServices.forEach(service -> service.init(entity));
        }
        return save;
    }

    private void loadInviteCode(T entity) {
        String inviteCode = entity.getInviteCode();
        if (StrUtils.isBlank(inviteCode)) {
            if (("," + EnumAuthDict.REGISTER_PROPS_REQUIRE.getValueCacheOrDefault("") + ",").contains(",inviteCode,")) {
                throw new BusinessException("邀请码必填");
            }
        } else {
            ILoginInfoPrepare loginInfo = UserOtherService.inviteCodeServices.stream().map(service -> service.getByCode(inviteCode)).filter(Objects::nonNull).findFirst().orElse(null);
            if (loginInfo == null) {
                throw new BusinessException("无效的邀请码", inviteCode);
            } else {
                EnumSysRole sysRole = loginInfo.getSysRole();
                switch (sysRole) {
                    case USER:
                        T parentUser = (T) loginInfo;
                        entity.setPid(parentUser.getId());
                        entity.setPusername(parentUser.getUsername());
                        List<String> pids = Arrays.stream(parentUser.getPids().split(",")).filter(s -> !s.isEmpty()).collect(Collectors.toList());
//                        String pidsAppend = parentUser.getPids().isEmpty() ? "," : parentUser.getPids();
                        pids.add(parentUser.getId() + "");
                        entity.setPids("," + String.join(",", pids) + ",");
                        entity.setAgentId(parentUser.getAgentId());

                        // 直接上级在最前面
                        Collections.reverse(pids);
                        entity.setPid(Long.valueOf(pids.get(0)));
                        if (pids.size() >= 2) {
                            entity.setPid(Long.valueOf(pids.get(1)));
                        }
                        if (pids.size() >= 3) {
                            entity.setPid(Long.valueOf(pids.get(2)));
                        }
                        if (pids.size() >= 4) {
                            entity.setPid(Long.valueOf(pids.get(3)));
                        }
                        if (pids.size() >= 5) {
                            entity.setPid(Long.valueOf(pids.get(4)));
                        }
                        break;
                    case AGENT:
                        entity.setAgentId(loginInfo.getId());
                        break;
                }
            }
        }
    }

    @Override
    public boolean updateById(T entity) {
        this.doSaveOrUpdate(entity);
        return super.updateById(entity);
    }

    @Override
    public List<T> listByIds(Collection<? extends Serializable> userIds) {
        return userIds.isEmpty() ? Collections.emptyList() : super.listByIds(userIds);
    }

//    @Override
//    public List<T> listByIdsAndLevelIds(Collection<? extends Serializable> userIds, Collection<? extends Serializable> userLevelIds) {
//        return userIds.isEmpty() ? Collections.emptyList() : super.lambdaQuery().in(T::getId, userIds).in(T::getLevelId, userLevelIds).list();
//    }

    @Override
    public Map<Long, T> getMap(String queryProp, Collection<?> queryPropValues) {
        return getMap(queryProp, queryPropValues, T::getId);
    }

    @Override
    public List<Long> listIdByPids(List<Long> pids) {
        if (pids.isEmpty()) {
            return Collections.emptyList();
        }
        return this.lambdaQuery()
                .select(User::getId)
                .in(User::getPid, pids).list().stream().map(User::getId)
                .collect(Collectors.toList());
    }

    public void doSaveOrUpdate(T entity) {
        if (StrUtils.isNotBlank(entity.getPassword())) {
            this.checkOldPassword(entity, T::getPassword);
            entity.setPassword(PasswordUtils.encrypt_Password(entity.getPassword()));
        }
        if (StrUtils.isNotBlank(entity.getTkPassword())) {
            this.checkOldPassword(entity, T::getTkPassword);
            entity.setTkPassword(PasswordUtils.encrypt_tkPassword(entity.getTkPassword()));
        }
    }

    private boolean checkOldPassword(T entity, Function<T, String> passwordFun) {
        T entityOld = this.getById(entity.getId());
        if (entityOld == null) {
            return true;
        }
        switch (commonProperties.getApplication()) {
            case user:
                String passwordOld = passwordFun.apply(entityOld);
                AssertUtil.isTrue(StrUtils.isBlank(passwordOld) || passwordOld.equals(PasswordUtils.encrypt_Password(entity.getPasswordOld())), "请输入旧密码");
                break;
        }
        return true;
    }

    @Override
    public ILoginInfoPrepare getByCode(String code) {
        return this.lambdaQuery().eq(T::getCode, code).one();
    }

    @Override
    public IMultiPageResult<T> selectPage(String entityClassName, IPageInput pageInput, Map<String, Object> paramMap, TimeQuery timeQuery) {
        // 判断并过滤登录用户Id
        if (this.filterLoginUserIdsReturnEmptyFlag(paramMap)) {
            return new MultiPageResult<>();
        }

        return super.selectPage(entityClassName, pageInput, paramMap, timeQuery);
    }

    /**
     * 过滤在线userIds
     *
     * @return 在线userId是否为空
     */
    private boolean filterLoginUserIdsReturnEmptyFlag(Map<String, Object> paramMap) {
        Object isOnline = paramMap.get("isOnline");
        if (null == paramMap.get("id") && isOnline != null) {
            Set<Long> onlineUserIds = this.getOnlineUserIds();
            // 在登录列表中
            if (FrameworkConst.Str.TRUE.equals(isOnline)) {
                if (onlineUserIds.isEmpty()) {
                    // 无需额外过滤 直接返回空信息
                    return true;
                } else {
                    paramMap.put("id", "#in#" + onlineUserIds.stream().map(Objects::toString).collect(Collectors.joining("")));
                }
            }
            // 不在登录列表中
            else if (FrameworkConst.Str.FALSE.equals(isOnline)) {
                if (onlineUserIds.isEmpty()) {
                    // 无需额外过滤 继续基础查询
                    return false;
                } else {
                    paramMap.put("id", "#ni#" + onlineUserIds.stream().map(Objects::toString).collect(Collectors.joining("")));
                }
            }
        }
        return false;
    }

    @Override
    public List<AggregateResult> selectCountSum(String entityClassName, Map<String, Object> paramMap, TimeQuery timeQuery, GroupBy groupBy) {
        // 判断并过滤登录用户Id
        if (this.filterLoginUserIdsReturnEmptyFlag(paramMap)) {
            return Collections.emptyList();
        }
        return super.selectCountSum(entityClassName, paramMap, timeQuery, groupBy);
    }

    /**
     * 获取所有用户端在线人数id集合
     */
    public Set<Long> getOnlineUserIds() {
        Set<Long> userIdList = new HashSet<>(4096);
        Arrays.stream(StrUtil.blankToDefault(EnumAuthDict.FRONT_API.getValueCache(), EnumAuthDict.FRONT_DOMAIN.getValueCache() + "/api").split(",")).forEach(frontUrl -> {
            try {
                String result = HttpUtil.get(frontUrl + "/common" + CommonConst.Str.path_loginUserIds, 10000);
                if (StrUtil.isNotBlank(result)) {
                    Object data = JSONUtils.fromString(result, R.class).getData();
                    if (data instanceof String) {
                        userIdList.addAll(Arrays.stream(((String) data).split(",")).filter(s -> null != s && s.length() > 0).map(Long::valueOf).collect(Collectors.toSet()));
                        return;
                    }
                }
                log.warn("获取在线人数失败: {} {}", frontUrl, result);
            } catch (Exception e) {
                log.warn("{} 端在线人数异常", frontUrl, e);
            }
        });
        return userIdList;
    }
}
