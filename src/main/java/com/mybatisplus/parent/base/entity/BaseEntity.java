package com.mybatisplus.parent.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class BaseEntity extends AbstractBaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO) // 主键生成策略
    private Long id;
}
