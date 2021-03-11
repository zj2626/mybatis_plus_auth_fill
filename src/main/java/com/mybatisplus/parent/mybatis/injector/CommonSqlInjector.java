package com.mybatisplus.parent.mybatis.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.mybatisplus.parent.mybatis.method.InsertBatch;
import com.mybatisplus.parent.mybatis.method.LogicDeleteBatchByIdsWithFill;
import com.mybatisplus.parent.mybatis.method.LogicDeleteByIdWithFill;
import com.mybatisplus.parent.mybatis.method.LogicDeleteWithFill;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义Sql注入器, 为mapper添加公共的方法
 */
@Component
@ConditionalOnClass(DefaultSqlInjector.class)
public class CommonSqlInjector extends DefaultSqlInjector {

    // https://gitee.com/baomidou/mybatis-plus-samples/tree/master/mybatis-plus-sample-deluxe
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        final List<AbstractMethod> methodList = super.getMethodList(mapperClass);

        // 逻辑删除且添加更新字段 对应 BaseMapper中的 int deleteById(T entity);
        methodList.add(new LogicDeleteWithFill());
        methodList.add(new LogicDeleteByIdWithFill());
        methodList.add(new LogicDeleteBatchByIdsWithFill());
        methodList.add(new InsertBatch());

        return methodList;
    }
}