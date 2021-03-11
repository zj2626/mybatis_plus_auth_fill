/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.mybatisplus.parent.mybatis.method;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.mybatisplus.parent.mybatis.enums.CustomerSqlMethod;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Objects;

import static java.util.stream.Collectors.joining;


/**
 * 批量插入数据, 不能自动加入[插入填充:withInsertFill]的字段, 需要在service中指定
 */
public class InsertBatch extends AbstractMethod {

    private static final String ITEM = "item";
    private static final CustomerSqlMethod customerSqlMethod = CustomerSqlMethod.INSERT_BATCH;

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        KeyGenerator keyGenerator = new NoKeyGenerator();
        String keyProperty = null;
        String keyColumn = null;
        // 表包含主键处理逻辑,如果不包含主键当普通字段处理
        if (StringUtils.isNotBlank(tableInfo.getKeyProperty())) {
            if (tableInfo.getIdType() == IdType.AUTO) {
                /** 自增主键 */
                keyGenerator = new Jdbc3KeyGenerator();
                keyProperty = tableInfo.getKeyProperty();
                keyColumn = tableInfo.getKeyColumn();
            } else {
                if (null != tableInfo.getKeySequence()) {
                    keyGenerator = TableInfoHelper.genKeyGenerator(customerSqlMethod.getMethod(), tableInfo, builderAssistant);
                    keyProperty = tableInfo.getKeyProperty();
                    keyColumn = tableInfo.getKeyColumn();
                }
            }
        }

        // 批量插入sql: 插入Column字段
        String allInsertSqlColumn = tableInfo.getKeyInsertSqlColumn(true) +
                tableInfo.getFieldList().stream().map(TableFieldInfo::getInsertSqlColumn).filter(Objects::nonNull).collect(joining(NEWLINE));
        String columnScript = SqlScriptUtils.convertTrim(allInsertSqlColumn, LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);

        // 批量插入sql: 插入Property字段
        final String newPrefix = ITEM + DOT;
        String allInsertSqlProperty = tableInfo.getKeyInsertSqlProperty(newPrefix, true) +
                tableInfo.getFieldList().stream().map(i -> i.getInsertSqlProperty(newPrefix)).filter(Objects::nonNull).collect(joining(NEWLINE));
        String valuesScript = SqlScriptUtils.convertTrim(allInsertSqlProperty, LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
        final String convertForeach = SqlScriptUtils.convertForeach(valuesScript, COLLECTION, null, ITEM, COMMA);

        String sql = String.format(customerSqlMethod.getSql(),
                tableInfo.getTableName(),
                columnScript,
                convertForeach);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass, customerSqlMethod.getMethod(), sqlSource, keyGenerator, keyProperty, keyColumn);
    }

    @Override
    public String getMethod(SqlMethod sqlMethod) {
        // 自定义 mapper 方法名
        return customerSqlMethod.getMethod();
    }
}
