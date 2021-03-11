package com.mybatisplus.parent.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.mybatisplus.parent.base.bo.UserInfoBo;
import com.mybatisplus.parent.code.StringKeyCode;
import com.mybatisplus.parent.utils.CommonAdminUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 起始版本 3.3.0; 默认有值不覆盖,如果提供的值为null则填充
 */
@Slf4j
@Component
@ConditionalOnClass(MetaObjectHandler.class)
public class MyMetaObjectHandler implements MetaObjectHandler {

    private static Logger logger = LoggerFactory.getLogger(MyMetaObjectHandler.class);

    private static final ThreadLocal<UserInfoBo> USER_INFO_IN_SYSTEM = ThreadLocal.withInitial(CommonAdminUtils::getSimpleUserFromSystem);

    /**
     * 自动填充-新增数据填充
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            // 批量新增特殊处理 @see com.mybatisplus.parent.base.service.impl.BaseServiceImpl.saveBatch(java.util.Collection<T>, int, boolean)
            final UserInfoBo userInfoBo = MyMetaObjectHandler.USER_INFO_IN_SYSTEM.get();

            this.strictInsertFill(metaObject, StringKeyCode.CREATE_TIME, Date.class, new Date());
            this.strictInsertFill(metaObject, StringKeyCode.CREATE_USER, String.class, userInfoBo.getLoginName());
            this.strictInsertFill(metaObject, StringKeyCode.CREATE_USER_ID, Long.class, userInfoBo.getId());

            this.strictInsertFill(metaObject, StringKeyCode.UPDATE_TIME, Date.class, new Date());
            this.strictInsertFill(metaObject, StringKeyCode.UPDATE_USER, String.class, userInfoBo.getLoginName());
            this.strictInsertFill(metaObject, StringKeyCode.UPDATE_USER_ID, Long.class, userInfoBo.getId());

            this.strictInsertFill(metaObject, StringKeyCode.DELETE_STATUS, Boolean.class, true);
        } catch (Exception e) {
            logger.error("填充新增字段异常: {}", e.getMessage(), e);
        } finally {
            clearThreadLocal();
        }
    }

    /**
     * 自动填充-更新数据填充
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            final UserInfoBo userInfoBo = MyMetaObjectHandler.USER_INFO_IN_SYSTEM.get();

            this.strictUpdateFill(metaObject, StringKeyCode.UPDATE_TIME, Date.class, new Date());
            this.strictUpdateFill(metaObject, StringKeyCode.UPDATE_USER, String.class, userInfoBo.getLoginName());
            this.strictUpdateFill(metaObject, StringKeyCode.UPDATE_USER_ID, Long.class, userInfoBo.getId());
        } catch (Exception e) {
            logger.error("填充修改字段异常: {}", e.getMessage(), e);
        } finally {
            clearThreadLocal();
        }
    }

    // 要填充的情况: 1.字段为空 2.缓冲中要填充内容不是未登录
    @Override
    public MetaObjectHandler strictFillStrategy(MetaObject metaObject, String fieldName, Supplier<Object> fieldVal) {
        if (metaObject.getValue(fieldName) == null
                || UserInfoBo.isNotEmpty(MyMetaObjectHandler.USER_INFO_IN_SYSTEM.get())) {
            Object obj = fieldVal.get();
            if (Objects.nonNull(obj)) {
                metaObject.setValue(fieldName, obj);
            }
        }
        return this;
    }

    public void clearThreadLocal() {
        MyMetaObjectHandler.USER_INFO_IN_SYSTEM.remove();
    }
}