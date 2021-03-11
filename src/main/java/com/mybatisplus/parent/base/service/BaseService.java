package com.mybatisplus.parent.base.service;

import com.baomidou.mybatisplus.extension.service.IService;

public interface BaseService<T> extends IService<T> {

    Class<T> getEntityClass();

}
