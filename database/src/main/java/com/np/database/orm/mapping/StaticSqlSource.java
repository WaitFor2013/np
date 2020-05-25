package com.np.database.orm.mapping;

import java.util.List;

/**
 * @author Clinton Begin
 */
public class StaticSqlSource implements SqlSource {

    private final String sql;
    private final List<ColumnMapping> parameterMappings;

    public StaticSqlSource(String sql) {
        this(sql, null);
    }

    public StaticSqlSource(String sql, List<ColumnMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return new BoundSql(sql, parameterMappings, parameterObject);
    }

}
