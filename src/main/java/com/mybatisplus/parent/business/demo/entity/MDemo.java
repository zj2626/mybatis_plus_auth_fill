package com.mybatisplus.parent.business.demo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mybatisplus.parent.base.entity.BaseEntity;
import com.mybatisplus.parent.mybatis.annotation.TableQueryField;
import lombok.Data;

/**
 * <p>
 * </p>
 *
 * @author zj2626
 * @since 2020-03-18
 */
@Data
@TableName("m_demo")
public class MDemo extends BaseEntity {

    private String name;

    private String province;
    private String city;
    private String area;
    private String address;

    /*
     * 公司ID
     */
    @TableQueryField
    @TableField(fill = FieldFill.INSERT)
    private Long companyId;
}
