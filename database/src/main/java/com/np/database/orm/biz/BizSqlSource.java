package com.np.database.orm.biz;

import com.np.database.exception.NpDbException;
import com.np.database.orm.SqlCommandType;
import com.np.database.orm.biz.db.render.PostgreSQLRender;
import com.np.database.orm.biz.param.BizParam;
import com.np.database.orm.mapping.BoundSql;
import com.np.database.orm.mapping.ColumnMapping;
import com.np.database.orm.mapping.SqlSource;
import com.np.database.orm.biz.db.render.PostgreSQLRender;
import com.np.database.orm.biz.param.BizParam;
import com.np.database.orm.mapping.BoundSql;
import com.np.database.orm.mapping.ColumnMapping;
import com.np.database.orm.mapping.SqlSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ch
 */
public class BizSqlSource implements SqlSource {

    private final List<ColumnMapping> allParams;
    private final DbTypeEnum dbTypeEnum;

    private SqlCommandType sqlCommandType;
    private String tableName;
    private String noWhereQuerySql;
    private boolean isCount = false;


    public BizSqlSource(DbTypeEnum dbTypeEnum, List<ColumnMapping> allParams) {
        if (null == dbTypeEnum || null == allParams) {
            throw new NpDbException("dbTypeEnum and allParams can't be null.");
        }

        this.dbTypeEnum = dbTypeEnum;
        this.allParams = allParams;
    }

    public void initSelect(String noWhereSql) {
        sqlCommandType = SqlCommandType.SELECT;
        this.noWhereQuerySql = noWhereSql;
    }

    public void initCount(String noWhereSql) {
        sqlCommandType = SqlCommandType.SELECT;
        this.noWhereQuerySql = noWhereSql;
        this.isCount = true;
    }

    public void initInsert(String tableName) {
        sqlCommandType = SqlCommandType.INSERT;
        this.tableName = tableName;
    }

    public void initDelete(String tableName) {
        sqlCommandType = SqlCommandType.DELETE;
        this.tableName = tableName;
    }

    public void initUpdate(String tableName) {
        sqlCommandType = SqlCommandType.UPDATE;
        this.tableName = tableName;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        validate(parameterObject);

        Map<String, ColumnMapping> pMap = new HashMap<>();
        allParams.forEach(parameterMapping -> pMap.put(parameterMapping.getColumn(), parameterMapping));

        BizSqlRender sqlRender;
        switch (dbTypeEnum) {
            case POSTGRESQL:
                sqlRender = PostgreSQLRender.INSTANCE;
                break;
            default:
                throw new NpDbException("BizSqlSource now just support POSTGRESQL");
        }

        BizParam bizParam = (BizParam) parameterObject;

        switch (sqlCommandType) {
            case SELECT:
                return sqlRender.renderQuery(noWhereQuerySql, pMap, bizParam, isCount);
            case UPDATE:
                return sqlRender.renderUpdate(tableName, pMap, bizParam);
            case DELETE:
                return sqlRender.renderDelete(tableName, pMap, bizParam);
            case INSERT:
                return sqlRender.renderInsert(tableName, pMap, bizParam);
            default:
                throw new NpDbException("unsupported sqlCommandType:" + sqlCommandType);
        }

    }

    private void validate(Object parameterObject) {
        if (null == parameterObject || null == sqlCommandType) {
            throw new NpDbException("BizSqlSource parameterObject and sqlCommandType can't be null.");
        }
        if (null == tableName && (
                SqlCommandType.DELETE == sqlCommandType || SqlCommandType.INSERT == sqlCommandType || SqlCommandType.UPDATE == sqlCommandType)) {
            throw new NpDbException("tableName can't be null when insert or update or delete");
        }

        if (!(parameterObject instanceof BizParam)) {
            throw new NpDbException("parameterObject must be BizParam ");
        }

    }
}
