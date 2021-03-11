package com.mybatisplus.parent.utils;

import com.mybatisplus.parent.base.bo.UserInfoBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonAdminUtils {
    protected static final Logger logger = LoggerFactory.getLogger(CommonAdminUtils.class);

    public static Long getCompanyIdFromUserInfoBo() {
        // TODO 来源于 com.mybatisplus.parent.base.bo.UserInfoBo
        return -1L;
    }

    /**
     * 直接获取当前登录用户信息-基础信息
     *
     * @return
     */
    public static UserInfoBo getSimpleUserFromSystem() {
        // TODO 自己实现
        return null;
    }
}
