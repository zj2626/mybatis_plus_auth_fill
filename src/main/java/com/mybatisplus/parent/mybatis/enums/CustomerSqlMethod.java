package com.mybatisplus.parent.mybatis.enums;

/**
 * @author zj2626
 * @name CustomerSqlMethod
 * @description
 * @create 2020-12-19 16:41
 **/
public enum CustomerSqlMethod {
    INSERT_BATCH("insertBatch", "批量插入数据", "<script>\nINSERT INTO %s %s VALUES %s\n</script>"),
    ;

    private final String method;
    private final String desc;
    private final String sql;

    CustomerSqlMethod(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

    public String getMethod() {
        return method;
    }

    public String getDesc() {
        return desc;
    }

    public String getSql() {
        return sql;
    }
}
