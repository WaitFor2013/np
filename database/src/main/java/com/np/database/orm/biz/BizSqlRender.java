package com.np.database.orm.biz;

import com.np.database.orm.biz.param.BizParam;
import com.np.database.orm.mapping.BoundSql;
import com.np.database.orm.mapping.ColumnMapping;
import com.np.database.orm.biz.param.BizParam;
import com.np.database.orm.mapping.BoundSql;
import com.np.database.orm.mapping.ColumnMapping;

import java.util.List;
import java.util.Map;

/**
 * 参数渲染
 */
public interface BizSqlRender {

    //query,size,order,limit support
    BoundSql renderQuery(String sql, Map<String, ColumnMapping> parameterMappings, BizParam param, boolean isCount);

    //update
    BoundSql renderUpdate(String tableName, Map<String, ColumnMapping> parameterMappings, BizParam param);

    //delete
    BoundSql renderDelete(String tableName, Map<String, ColumnMapping> parameterMappings, BizParam param);

    //insert
    BoundSql renderInsert(String tableName, Map<String, ColumnMapping> parameterMappings, BizParam param);

    static String renderSimpleSelect(String tableName, List<ColumnMapping> columnMappings) {
        StringBuilder simpleSql = new StringBuilder(" SELECT ");

        for (int i = 0; i < columnMappings.size(); i++) {
            ColumnMapping columnMapping = columnMappings.get(i);

            if (i != 0) {
                simpleSql.append(",");
            }
            if (null != columnMapping.getColumnPrefix() && !columnMapping.getColumnPrefix().isEmpty()) {
                simpleSql.append(columnMapping.getColumnPrefix()).append(".");
            }
            simpleSql.append(columnMapping.getColumn());
        }

        simpleSql.append(" FROM ").append(tableName);

        return simpleSql.toString();
    }
}
