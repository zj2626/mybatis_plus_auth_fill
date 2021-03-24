package com.mybatisplus.parent.base.service.impl;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.mybatisplus.parent.base.entity.BaseEntity;
import com.mybatisplus.parent.base.mapper.BaseMapper;
import com.mybatisplus.parent.base.service.BaseService;
import com.mybatisplus.parent.utils.CommonAdminUtils;
import com.mybatisplus.parent.base.bo.UserInfoBo;
import com.mybatisplus.parent.code.StringKeyCode;
import com.mybatisplus.parent.mybatis.interceptor.MybatisSqlQueryInterceptor;
import com.mybatisplus.parent.utils.TransformUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description service层基类
 * @date 2020/4/21
 */
public abstract class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T> {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Class<T> getEntityClass() {
        Type type = this.getClass().getGenericSuperclass();
        //ParameterizedType参数化类型，即泛型
        ParameterizedType p = (ParameterizedType) type;
        //获取第二个参数，下标从0开始
        return (Class<T>) p.getActualTypeArguments()[1];
    }

    /**
     * @return T 实例化对象
     * @description 获取子类传入泛型实例
     * @author chentuan
     * @date 2020/4/21
     */
    private T getChildType() {
        T t = null;
        try {
            t = getEntityClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * @param id 删除表主键id
     * @return boolean
     * @description 重写父类方法，已经断言
     * @author chentuan
     * @date 2020/4/21
     */
    @Override
    public boolean removeById(Serializable id) {
        T entity = getChildType();
        entity.setId((Long) id);
        return SqlHelper.retBool(getBaseMapper().deleteByIdWithFill(entity));
    }

    /**
     * @param wrapper 删除条件
     * @return boolean
     * @description 已断言，继承父类方法
     * @author chentuan
     * @date 2020/4/21
     */
    @Override
    public boolean remove(Wrapper<T> wrapper) {
        return SqlHelper.retBool(getBaseMapper().deleteWithFill(getChildType(), wrapper));
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        try {
            return SqlHelper.retBool(getBaseMapper().deleteBatchByIdsWithFill(getChildType(), idList));
        } catch (Exception e) {
            logger.error("删除异常", e);
        }
        return false;
    }

    protected void assemble(T entity, LambdaQueryWrapper<T> wrapper) {
        if (null != entity.getId()) {
            wrapper.eq(T::getId, entity.getId());
        }
        // then do nothing
    }


    @Override
    public boolean update(Wrapper<T> updateWrapper) {
        T childType = getChildType();
        return update(childType, updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        if (CollectionUtils.isEmpty(entityList)) {
            return false;
        }
        boolean plus = 0 == entityList.size() % batchSize;
        int size = plus ? entityList.size() / batchSize : entityList.size() / batchSize + 1;

        //        Field companyIdField = null;
        //        try {
        //            Field declaredField = MybatisSqlQueryInterceptor.checkFieldWithTableField(entityList.toArray()[0].getClass());
        //            if (null != declaredField) {
        //                final TableField[] annotationsByType = declaredField.getAnnotationsByType(TableField.class);
        //                for (TableField tableField : annotationsByType) {
        //                    if (FieldFill.INSERT.equals(tableField.fill())) {
        //                        companyIdField = declaredField;
        //                        break;
        //                    }
        //                }
        //            }
        //        } catch (Exception ignore) {
        //        }

        final UserInfoBo userInfoBo = CommonAdminUtils.getSimpleUserFromSystem();

        for (int i = 0; i < size; i++) {
            final Collection<T> subEntityList = entityList.stream().skip(i * batchSize).limit(batchSize).collect(Collectors.toList());
            //            Field finalCompanyIdField = companyIdField;
            subEntityList.forEach(entity -> {
                if (null == entity.getCreateTime()) {
                    entity.setCreateTime(new Date());
                }
                if (null == entity.getCreateUserId()) {
                    entity.setCreateUserId(userInfoBo.getId());
                }
                if (null == entity.getCreateUser()) {
                    entity.setCreateUser(userInfoBo.getLoginName());
                }
                if (null == entity.getUpdateTime()) {
                    entity.setUpdateTime(new Date());
                }
                if (null == entity.getUpdateUserId()) {
                    entity.setUpdateUserId(userInfoBo.getId());
                }
                if (null == entity.getUpdateUser()) {
                    entity.setUpdateUser(userInfoBo.getLoginName());
                }
                if (null == entity.getCompanyId()) {
                    entity.setCompanyId(userInfoBo.getCompanyId());
                }
                //                try {
                //                    if (null != finalCompanyIdField) {
                //                        finalCompanyIdField.setAccessible(true);
                //                        if (null == finalCompanyIdField.get(entity)) {
                //                            finalCompanyIdField.set(entity, userInfoBo.getCompanyId());
                //                        }
                //                    }
                //                } catch (Exception e) {
                //                    logger.info("设置[{}]属性异常, {}", StringKeyCode.COMPANY_ID_NAME, e.getMessage());
                //                }

                entity.setDeleteStatus(true);
            });
            getBaseMapper().insertBatch(subEntityList);
        }
        return true;
    }

    private List<T> collectionToList(Collection<?> entityList) {
        T childType = getChildType();
        return entityList.stream().map(this::objToEitity).collect(Collectors.toList());
    }

    private T objToEitity(Object obj) {
        if (getEntityClass().isAssignableFrom(obj.getClass())) {
            return (T) obj;
        }
        return TransformUtils.copy(obj, getEntityClass());
    }
}
