package com.mybatisplus.parent.mybatis;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.mybatisplus.parent.mybatis.interceptor.MybatisSqlCompletePrintInterceptor;
import com.mybatisplus.parent.mybatis.interceptor.MybatisSqlQueryInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(PaginationInterceptor.class)
public class MybatisInterceptorConfig {
    /**
     * 注: 过于影响系统速度
     *
     * @param
     * @return com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer
     * @author zj2626
     * @date 2021/1/22
     */
    //    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> configuration.addInterceptor(new MybatisSqlCompletePrintInterceptor());
    }

    @Bean
    public ConfigurationCustomizer mybatisSqlQueryInterceptorCustomizer() {
        return configuration -> configuration.addInterceptor(new MybatisSqlQueryInterceptor());
    }
}