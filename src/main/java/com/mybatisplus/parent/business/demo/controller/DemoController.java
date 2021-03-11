package com.mybatisplus.parent.business.demo.controller;

import com.mybatisplus.parent.business.demo.entity.MDemo;
import com.mybatisplus.parent.business.demo.service.MDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 **/
@RequestMapping("/api")
@RestController
public class DemoController {

    @Autowired
    protected MDemoService service;

    @GetMapping(value = "/query")
    public String query(MDemo dto) {
        System.out.println(service.getById(dto.getId()));
        return "success";
    }
}
