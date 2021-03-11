package com.mybatisplus.parent.base.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.mybatisplus.parent.base.entity.BaseEntity;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public interface BaseMapper<T extends BaseEntity> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {

    /**
     * 批量插入数据, 不能自动加入[插入填充:withInsertFill]的字段, 需要在service[BaseService]中指定
     *
     * @param entityList
     * @return
     */
    int insertBatch(@Param(Constants.COLLECTION) Collection<T> entityList);

    /**
     * 逻辑删除且添加更新字段 [通过ID删除对应数据, entity需要传实体(需要设置id属性)]
     *
     * @param entity
     * @return
     */
    int deleteByIdWithFill(T entity);

    /**
     * 批量逻辑删除且添加更新字段 [通过ID集合删除对应数据, entity需要传实体(不需要设置任何属性)]
     *
     * @param entity
     * @param idList
     * @return
     */
    int deleteBatchByIdsWithFill(@Param(Constants.ENTITY) T entity, @Param(Constants.COLLECTION) Collection<? extends Serializable> idList);

    /**
     * 逻辑删除且添加更新字段 [通过wrapper条件删除对应数据, entity需要传实体(不需要设置任何属性)]
     *
     * @param entity
     * @param wrapper
     * @return
     */
    int deleteWithFill(@Param(Constants.ENTITY) T entity, @Param(Constants.WRAPPER) Wrapper<T> wrapper);

    /**
     * 使用上面的两个或者service接口中的方法
     *
     * @param id
     * @return
     */
    @Override
    @Deprecated
    int deleteById(Serializable id);

    @Override
    @Deprecated
    int deleteByMap(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);

    @Override
    @Deprecated
    int delete(@Param(Constants.WRAPPER) Wrapper<T> wrapper);

    @Override
    @Deprecated
    int deleteBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);
}
