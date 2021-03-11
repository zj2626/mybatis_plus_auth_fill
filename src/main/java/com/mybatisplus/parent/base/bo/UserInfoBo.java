package com.mybatisplus.parent.base.bo;

import com.mybatisplus.parent.code.StringKeyCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoBo {

    private static UserInfoBo EMPTY;

    private boolean isEmpty = false;

    private Long id;
    private String loginPhone;
    private String loginName;
    private Long companyId;

    static {
        EMPTY = new UserInfoBo();
        EMPTY.setEmpty(true);
        EMPTY.setId(-1L);
        EMPTY.setLoginPhone(StringKeyCode.UNKNOWN);
        EMPTY.setLoginName(StringKeyCode.UNKNOWN);
        EMPTY.setCompanyId(-1L);
    }

    public static boolean isNotEmpty(UserInfoBo userInfoBo) {
        return !isEmpty(userInfoBo);
    }

    public static boolean isEmpty(UserInfoBo userInfoBo) {
        return null == userInfoBo || userInfoBo.isEmpty();
    }

    public static UserInfoBo empty() {
        return EMPTY;
    }
}
