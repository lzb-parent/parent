package com.pro.common.module.service.usermoney.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pro.common.module.api.user.intf.IUserService;
import com.pro.common.module.api.user.model.db.User;
import com.pro.common.module.api.usermoney.model.dto.AmountEntityUnitDTO;
import com.pro.common.module.api.usermoney.model.enums.EnumAmountNegativeDeal;
import com.pro.common.module.api.usermoney.model.modelbase.AmountEntity;
import com.pro.common.module.api.usermoney.model.modelbase.AmountEntityRecord;
import com.pro.common.module.api.usermoney.model.modelbase.intf.IAmountEntityRecord;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.framework.api.enums.EnumAmountUpDown;
import com.pro.framework.api.util.AssertUtil;
import com.pro.framework.api.util.BeanUtils;
import com.pro.framework.api.util.LambdaUtil;
import com.pro.framework.api.util.StrUtils;
import com.pro.framework.api.util.inner.FunSerializable;
import com.pro.framework.api.util.inner.SerializedLambdaData;
import com.pro.framework.mybatisplus.wrapper.MyWrappers;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数额变化的基础类
 */
@SuppressWarnings({"rawtypes", "unchecked", "deprecation"})
//@Service
@Slf4j
public abstract class AmountEntityUnitService<Entity extends AmountEntity, Record extends AmountEntityRecord, DTO extends IAmountEntityRecord> {
    @Autowired
    private IUserService userService;
    @Autowired
    private RedisLockRegistry redisLockRegistry;

    /**
     * 数额更变
     */
    @Transactional(rollbackFor = Exception.class)
    public Record change(EnumAmountNegativeDeal negativeDeal, DTO record) {
        return change(negativeDeal, record, true);
    }

    /**
     * 数额更变
     */
    @Transactional(rollbackFor = Exception.class)
    public Record change(EnumAmountNegativeDeal negativeDeal, DTO record, Boolean needSaveRecords) {
        if (negativeDeal == null) {
            throw new BusinessException("amount change | negativeDeal can not be null | ");
        }
        List<Record> records = this.change(new AmountEntityUnitDTO<>(negativeDeal, null, Collections.singletonList(record), needSaveRecords));
        return records.size() > 0 ? records.get(0) : null;
    }

    /**
     * 数额更变(批量)
     */
    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public List<Record> change(AmountEntityUnitDTO<DTO> dto) {
        LocalDateTime now = LocalDateTime.now();
        Boolean needSaveRecords = dto.getNeedSaveRecords();
        EnumAmountNegativeDeal negativeDeal = dto.getNegativeDeal();
        DTO recordCommonInfo = dto.getRecordCommonInfo();
        List<DTO> recordBaseList = dto.getRecordList();
        if (CollUtil.isEmpty(recordBaseList)) {
            return Collections.emptyList();
        }
        String recordClassName = recordBaseList.get(0).getClass().getSimpleName();
        if (recordCommonInfo != null) {
            for (DTO recordBase : recordBaseList) {
                BeanUtils.copyPropertiesModel(recordCommonInfo, recordBase);
            }
        }
        for (DTO recordBase : recordBaseList) {
            BigDecimal amount = recordBase.getAmount();
            if (recordBase.getEntityKey() == null) {
                throw new BusinessException("amount change | entityKey can not be null | " + recordClassName);
            }
            if (recordBase.getUserId() == null) {
                throw new BusinessException("amount change | can not be null | " + recordClassName);
            }
            if (recordBase.getUpDown() == null) {
                throw new BusinessException("amount change | up or down ? | " + recordClassName);
            }
            if (null == amount || amount.compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException("The size amount (change amount) is negative and meaningless!");
            }
        }

        Set<Long> userIds = recordBaseList.stream().map(DTO::getUserId).collect(Collectors.toSet());
        Set<String> entityKeys = recordBaseList.stream().map(DTO::getEntityKey).collect(Collectors.toSet());
        Boolean optimisticLocking = entityKeys.size() < 5;
        List<Record> recordNewList = new ArrayList<>();

        Lock lock = null;
        //悲观锁
        if (!optimisticLocking) {
            lock = redisLockRegistry.obtain(recordClassName);
            boolean isLocked = lock.tryLock(5, TimeUnit.SECONDS);
            if (!isLocked) {
                log.error("无法获取悲观锁 " + recordClassName);
                throw new BusinessException("retry later");
            }
        }
        try {
            // 1.查询(或新增(一般要唯一索引))数量实体,添加锁
            // 如果记录<=5,用乐观事务锁 (大部分简单事务)
            // 如果记录>5,用悲观观事务锁
            List<Entity> entityList = this.listEntitysNoExistCreate(userIds, entityKeys, optimisticLocking);
            entityList.forEach(entity -> entity.setAmountChangeInit(entity.getAmount()));
            Map<String, Entity> entityMap = entityList.stream().collect(Collectors.toMap(Entity::getEntityKey, e -> e));
//            Map<String, List<DTO>> groupByEntitys = recordBaseList.stream().collect(Collectors.groupingBy(DTO::getEntityKey));
//            groupByEntitys.forEach((entityKey, subList) -> {
//
//            });
            // 2.做更变
            for (DTO recordBase : recordBaseList) {
                Entity entity = entityMap.get(recordBase.getEntityKey());
                Map<String, Function<Entity, Object>> propMap = entity.getPropMap();
                if (propMap == null) {
                    propMap = new HashMap<>();
                    entity.setPropMap(propMap);
                }

                EnumAmountUpDown upDown = recordBase.getUpDown();


                BigDecimal amount = recordBase.getAmount();

                BigDecimal before = entity.getAmount();
                boolean isDown = EnumAmountUpDown.down.equals(upDown);
                BigDecimal after = isDown ? before.subtract(amount) : before.add(amount);
                if (isDown && after.compareTo(BigDecimal.ZERO) < 0) {
                    switch (negativeDeal) {
                        case toNegative:
                            break;
                        case toZero:
                            after = BigDecimal.ZERO;
                            break;
                        case throwException:
                            log.warn(StrUtil.format("{}的余额为{}，不足以扣除{}", entity.getUsername(), entity.getAmount(), amount));
                            throw new BusinessException("账户余额不足");
                    }
                }
                entity.setAmount(after);
                putProp(propMap, Entity::getAmount);
                switch (upDown) {
                    case up:
                        entity.setTodayIncreaseAmount(entity.getTodayIncreaseAmount().add(amount));
                        entity.setWeeklyIncreaseAmount(entity.getWeeklyIncreaseAmount().add(amount));
                        entity.setMonthIncreaseAmount(entity.getMonthIncreaseAmount().add(amount));
                        entity.setYearIncreaseAmount(entity.getYearIncreaseAmount().add(amount));
                        entity.setTotalIncreaseAmount(entity.getTotalIncreaseAmount().add(amount));

                        entity.setTodayIncreaseTimes(entity.getTodayIncreaseTimes() + 1);
                        entity.setWeeklyIncreaseTimes(entity.getWeeklyIncreaseTimes() + 1);
                        entity.setMonthIncreaseTimes(entity.getMonthIncreaseTimes() + 1);
                        entity.setYearIncreaseTimes(entity.getYearIncreaseTimes() + 1);
                        entity.setTotalIncreaseTimes(entity.getTotalIncreaseTimes() + 1);

                        putProp(propMap, Entity::getTodayIncreaseAmount);
                        putProp(propMap, Entity::getWeeklyIncreaseAmount);
                        putProp(propMap, Entity::getMonthIncreaseAmount);
                        putProp(propMap, Entity::getYearIncreaseAmount);
                        putProp(propMap, Entity::getTotalIncreaseAmount);

                        putProp(propMap, Entity::getTodayIncreaseTimes);
                        putProp(propMap, Entity::getWeeklyIncreaseTimes);
                        putProp(propMap, Entity::getMonthIncreaseTimes);
                        putProp(propMap, Entity::getYearIncreaseTimes);
                        putProp(propMap, Entity::getTotalIncreaseTimes);
                        break;
                    case down:
                        entity.setTodayDecreaseAmount(entity.getTodayDecreaseAmount().add(amount));
                        entity.setWeeklyDecreaseAmount(entity.getWeeklyDecreaseAmount().add(amount));
                        entity.setMonthDecreaseAmount(entity.getMonthDecreaseAmount().add(amount));
                        entity.setYearDecreaseAmount(entity.getYearDecreaseAmount().add(amount));
                        entity.setTotalDecreaseAmount(entity.getTotalDecreaseAmount().add(amount));

                        entity.setTodayDecreaseTimes(entity.getTodayDecreaseTimes() + 1);
                        entity.setWeeklyDecreaseTimes(entity.getWeeklyDecreaseTimes() + 1);
                        entity.setMonthDecreaseTimes(entity.getMonthDecreaseTimes() + 1);
                        entity.setYearDecreaseTimes(entity.getYearDecreaseTimes() + 1);
                        entity.setTotalDecreaseTimes(entity.getTotalDecreaseTimes() + 1);

                        putProp(propMap, Entity::getTodayDecreaseAmount);
                        putProp(propMap, Entity::getWeeklyDecreaseAmount);
                        putProp(propMap, Entity::getMonthDecreaseAmount);
                        putProp(propMap, Entity::getYearDecreaseAmount);
                        putProp(propMap, Entity::getTotalDecreaseAmount);

                        putProp(propMap, Entity::getTodayDecreaseTimes);
                        putProp(propMap, Entity::getWeeklyDecreaseTimes);
                        putProp(propMap, Entity::getMonthDecreaseTimes);
                        putProp(propMap, Entity::getYearDecreaseTimes);
                        putProp(propMap, Entity::getTotalDecreaseTimes);
                        break;
                }

                if (needSaveRecords) {
                    Record recordNew = this.newRecord();
                    recordNewList.add(recordNew);

                    BeanUtils.copyPropertiesModel(entity, recordNew);
                    BeanUtils.copyPropertiesModel(recordBase, recordNew);
                    recordNew.setRecordType(recordBase.getRecordType().toString());
                    recordNew.setUpDown(recordBase.getUpDown());
                    recordNew.setAmountBefore(before);
                    recordNew.setAmountAfter(after);
                }
            }

            // 乐观
            if (optimisticLocking) {
                for (Entity entity : entityList) {
                    // 更新实体
                    boolean update = this.updateEntity(entity, now);
                    if (!update) {
                        log.error("乐观锁更新异常 {} entity={}\n  init={} propMap={} dto={} now={}", getClass(), JSONUtil.toJsonStr(entity), entity.getAmountChangeInit(), entity.getPropMap().keySet(), JSONUtil.toJsonStr(dto), now);
                        throw new BusinessException("please retry later"); // 发生并发了
                    } else {
                        log.info("乐观锁更新OK {} entity={}\n  init={} propMap={} dto={} now={}", getClass(), JSONUtil.toJsonStr(entity), entity.getAmountChangeInit(), entity.getPropMap().keySet(), JSONUtil.toJsonStr(dto), now);
                    }
                }
            }
            // 悲观
            else {
                // 选取有变化的字段,去更新
                List<Entity> entityListUpdate = entityList.stream().map(e ->
                        {
                            Entity updateEntity = newEntity();
                            updateEntity.setId(e.getId());
                            e.getPropMap().forEach((propName, propFun) -> {
                                Object propValue = propFun.apply(e);
                                BeanUtil.setProperty(updateEntity, propName, propValue);
                            });
                            return updateEntity;
                        }
                ).collect(Collectors.toList());
                // 更新实体
                this.getAmountEntityService().updateBatchById(entityListUpdate);
            }
            // 新增记录
            getAmountEntityRecordService().saveBatch(recordNewList);
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
        return recordNewList;
    }

    private void putProp(Map<String, Function<Entity, Object>> propMap, FunSerializable<Entity, Object> propFun) {
        SerializedLambdaData resolve = LambdaUtil.resolveCache(propFun);
        propMap.put(resolve.getPropName(), propFun);
    }

    private List<Entity> listEntitysNoExistCreate(Set<Long> userIds, Set<String> entityKeys, Boolean optimisticLocking) {
        List<Entity> entities = this.listEntity(userIds, optimisticLocking);
        List<Entity> finalEntities = entities;
        List<String> keyNoExists = entityKeys.stream().filter(key -> finalEntities.stream().noneMatch(e -> e.getEntityKey().equals(key))).collect(Collectors.toList());
        if (keyNoExists.size() > 0) {
            this.saveEntitys(keyNoExists);
            entities = this.listEntity(userIds, optimisticLocking);
        }
        entities = entities.stream().filter(e -> entityKeys.contains(e.getEntityKey())).collect(Collectors.toList());
        return entities;
    }


    private void saveEntitys(List<String> noExistKeys) {
        List<Entity> newEntitys = noExistKeys.stream().map(key ->
                {
                    Entity entity = newEntity();
                    entity.setEntityKey(key);
                    return entity;
                }
        ).collect(Collectors.toList());
        this.fillNewEntityInfos(newEntitys);
        this.saveBatchEntity(newEntitys);
//        return listEntity(newEntitys.stream().map(Entity::getUserId).collect(Collectors.toSet()), optimisticLocking);
    }


    @SneakyThrows
    protected Entity newEntity() {
        Class class1 = (Class) TypeUtil.getTypeArgument(getClass(), 0);
        return (Entity) class1.newInstance();
    }

    @SneakyThrows
    protected Record newRecord() {
        Class class1 = (Class) TypeUtil.getTypeArgument(getClass(), 1);
        return (Record) class1.newInstance();
    }

    public List<Entity> listEntity(Set<Long> userIds) {
        return listEntity(userIds, false);
    }

    protected List<Entity> listEntity(Set<Long> userIds, Boolean optimisticLocking) {
        return this.getAmountEntityService().lambdaQuery()
                .in(Entity::getUserId, userIds)
                .last(!optimisticLocking, " for update")
                .orderByAsc(Entity::getUserId)
                .list();
    }

    @SuppressWarnings("unused")
    protected void fillNewEntityInfos(List<Entity> newEntitys) {
        List<Long> userIds = newEntitys.stream().map(Entity::getUserId).collect(Collectors.toList());
        Map<Long, User> userMap = userService.idMap(userIds);
        for (Entity newEntity : newEntitys) {
            User user = userMap.get(newEntity.getUserId());
            fillNewEntityInfos(newEntity, user);
        }
    }

    private void fillNewEntityInfos(Entity newEntity, User user) {
        AssertUtil.notEmpty(user, "用户不存在_");
        BeanUtils.copyPropertiesModel(user, newEntity);
        newEntity.setUserId(user.getId());
    }

    protected void saveBatchEntity(List<Entity> newEntitys) {
        this.getAmountEntityService().saveBatch(newEntitys);
    }

    protected boolean updateEntity(Entity entity, LocalDateTime now) {
        UpdateWrapper<Entity> wrapper = MyWrappers.update();
        entity.getPropMap().forEach((propName, propFun) -> {
            Object propValue = propFun.apply(entity);
            wrapper.set(StrUtils.camelToUnderline(propName), propValue);
        });
        wrapper.lambda()
                .set(Entity::getUpdateTime, now)
                .eq(Entity::getId, entity.getId())
                .eq(Entity::getAmount, entity.getAmountChangeInit())
        ;
        return getAmountEntityService().update(wrapper);
    }

    public abstract IService<Entity> getAmountEntityService();

    public abstract IService<Record> getAmountEntityRecordService();

}
