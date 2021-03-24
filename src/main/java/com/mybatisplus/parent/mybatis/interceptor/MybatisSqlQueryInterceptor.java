package com.mybatisplus.parent.mybatis.interceptor;


import com.baomidou.mybatisplus.annotation.TableField;
import com.mybatisplus.parent.code.StringKeyCode;
import com.mybatisplus.parent.mybatis.annotation.TableQueryField;
import com.mybatisplus.parent.utils.CommonAdminUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Properties;

/*
 *  查询or更新的时候拼接where条件: headquarter_group_id = xxx
 */
@Intercepts({
        //        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
})
public class MybatisSqlQueryInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(MybatisSqlQueryInterceptor.class);


    private static final String WHERE = "WHERE";


    /**
     * intercept 方法用来对拦截的sql进行具体的操作
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameterObject = args[1];

        // id为执行的mapper方法的全路径名，如com.mapper.UserMapper
        String id = ms.getId();

        // sql语句类型 select、delete、insert、update
        String sqlCommandType = ms.getSqlCommandType().toString();

        BoundSql boundSql = ms.getBoundSql(parameterObject);
        String origSql = boundSql.getSql();

        origSql = origSql.replace("where", WHERE);
        if (!origSql.contains(WHERE)) {
            return invocation.proceed();
        }
        String[] splits = origSql.split(WHERE);
        if (splits.length < 2) {
            return invocation.proceed();
        }
        if (origSql.contains(StringKeyCode.COMPANY_ID_PARAM) && origSql.lastIndexOf(StringKeyCode.COMPANY_ID_PARAM) > origSql.indexOf(WHERE)) {
            return invocation.proceed();
        }

        try {
            final Class<?> entityType = ms.getResultMaps().get(0).getType();
            if (null != entityType) {
                if (null == checkFieldWithTableQueryField(entityType)) {
                    return invocation.proceed();
                }
            }
        } catch (Exception e) {
            return invocation.proceed();
        }

        // 组装新的 sql
        StringBuilder newSql = new StringBuilder();
        for (int i = 0; i < splits.length; i++) {
            if (i == splits.length - 1) {
                newSql.append(WHERE).append(" ").append(StringKeyCode.COMPANY_ID_PARAM).append(" = ").append(CommonAdminUtils.getCompanyIdFromUserInfoBo()).append(" AND ");
            }
            newSql.append(splits[i]);
        }

        // 重新new一个查询语句对象
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), newSql.toString(),
                boundSql.getParameterMappings(), boundSql.getParameterObject());

        // 把新的查询放到statement里
        MappedStatement newMs = newMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }

        Object[] queryArgs = invocation.getArgs();
        queryArgs[0] = newMs;

        return invocation.proceed();
    }

    public static Field checkFieldWithTableQueryField(Class<?> entityType) throws NoSuchFieldException {
        Field declaredField = entityType.getDeclaredField(StringKeyCode.COMPANY_ID_NAME);
        if (declaredField.isAnnotationPresent(TableQueryField.class)) {
            return declaredField;
        }
        return null;
    }

    public static Field checkFieldWithTableField(Class<?> entityType) throws NoSuchFieldException {
        Field declaredField = entityType.getDeclaredField(StringKeyCode.COMPANY_ID_NAME);
        if (declaredField.isAnnotationPresent(TableField.class)) {
            return declaredField;
        }
        return null;
    }

    /**
     * 定义一个内部辅助类，作用是包装 SQL
     */
    class BoundSqlSqlSource implements SqlSource {

        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }

    }

    private MappedStatement newMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new
                MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(ms.getKeyProperties()[0]);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;

    }

    @Override
    public void setProperties(Properties properties) {
        // 获取属性
        // String value1 = properties.getProperty("prop1");
        logger.info("properties方法：{}", properties.toString());
    }
}