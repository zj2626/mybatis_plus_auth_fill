package com.mybatisplus.parent.business.demo.service.impl;

import com.mybatisplus.parent.base.service.impl.BaseServiceImpl;
import com.mybatisplus.parent.business.demo.entity.MDemo;
import com.mybatisplus.parent.business.demo.mapper.MDemoMapper;
import com.mybatisplus.parent.business.demo.service.MDemoService;
import org.springframework.stereotype.Service;

/**
 * @name MDemoImpl
 * @description
 * @create 2021-03-11 14:01
 **/
@Service
public class MDemoServiceImpl extends BaseServiceImpl<MDemoMapper, MDemo> implements MDemoService {
}
