package com.mybatisplus.parent;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@ComponentScan({"com.mybatisplus.parent"})
@MapperScan({"com.mybatisplus.parent.business.*.mapper"})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableConfigurationProperties
public class ApplicationRun {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationRun.class);

    public static void main(String[] args) {
        new SpringApplicationBuilder(ApplicationRun.class).run(args);
    }
}
