package com.np.design.domain.ddl;

import com.np.database.ColumnDefinition;
import com.np.database.NpDataType;
import com.np.database.orm.biz.param.BizParam;
import com.np.database.orm.session.SqlSession;
import com.np.database.recommend.SysColumn;
import com.np.design.domain.db.SchemaColumn;
import com.np.design.domain.db.SchemaTable;
import com.np.design.exception.NpException;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class PgConsistencyCheck {

    public final static Map<String, PgTable> pgTableMap = new HashMap<>();
    public final static Map<String, List<PgColumn>> pgColumnsMap = new HashMap<>();
    public final static Map<String, List<PgIndex>> pgIndexes = new HashMap<>();

    public static void init(SqlSession sqlSession) {
        pgTableMap.clear();
        pgColumnsMap.clear();
        pgIndexes.clear();

        BizParam tableQuery = BizParam.NEW()
                .equals(ColumnDefinition.name("schemaname"), "public");
        List<PgTable> dbTables = sqlSession.queryAll(tableQuery, PgTable.class);
        if (null != dbTables)
            dbTables.forEach(dbTable -> pgTableMap.put(dbTable.getTablename(), dbTable));

        BizParam columnQuery = BizParam.NEW()
                .equals(ColumnDefinition.name("table_schema"), "public");
        List<PgColumn> pgColumns = sqlSession.queryAll(columnQuery, PgColumn.class);

        if (null != pgColumns) {
            pgColumns.forEach(pgColumn -> {
                if (pgColumnsMap.containsKey(pgColumn.getTableName())) {
                    pgColumnsMap.get(pgColumn.getTableName()).add(pgColumn);
                } else {
                    List<PgColumn> pgColumnsTmp = new ArrayList<>();
                    pgColumnsTmp.add(pgColumn);
                    pgColumnsMap.put(pgColumn.getTableName(), pgColumnsTmp);
                }
            });
        }

        List<PgIndex> pgIndices = sqlSession.queryAll(tableQuery, PgIndex.class);
        if (null != pgIndices) {
            pgIndices.forEach(pgIndex -> {
                if (pgIndexes.containsKey(pgIndex.getTablename())) {
                    pgIndexes.get(pgIndex.getTablename()).add(pgIndex);
                } else {
                    List<PgIndex> pgIndicesTmp = new ArrayList<>();
                    pgIndicesTmp.add(pgIndex);
                    pgIndexes.put(pgIndex.getTablename(), pgIndicesTmp);
                }
            });
        }

    }

    public static boolean doCheck(SqlSession sqlSession, SchemaTable table, List<SchemaColumn> columns) {
        Object booleanObj = doCheckInternal(sqlSession, table, columns, true);
        return Boolean.parseBoolean(booleanObj.toString());
    }

    public static String doCheckSingle(SqlSession sqlSession, SchemaTable table, List<SchemaColumn> columns) {
        Object strObj = doCheckInternal(sqlSession, table, columns, false);
        return strObj.toString();
    }

    private static Object doCheckInternal(SqlSession sqlSession, SchemaTable table, List<SchemaColumn> columns, boolean fastReturn) {

        StringBuilder resultInfo = new StringBuilder();
        int idx = 1;

        //check table
        BizParam tableQuery = BizParam.NEW()
                .equals(ColumnDefinition.name("schemaname"), "public")
                .equals(ColumnDefinition.name("tablename"), table.getTableName());
        List<PgTable> dbTables;
        if (fastReturn) {
            dbTables = new ArrayList<>();
            PgTable pgTable = pgTableMap.get(table.getTableName());
            if (null != pgTable) {
                dbTables.add(pgTable);
            }
        } else {
            dbTables = sqlSession.queryAll(tableQuery, PgTable.class);
        }
        if (null == dbTables && dbTables.size() != 1) {
            if (fastReturn) return false;
            resultInfo.append(idx++).append(".").append("实体数据库中不存在此表。\n");
            return resultInfo.toString();
        }

        //check columns
        if (null == columns || columns.isEmpty()) {
            if (fastReturn) return false;
            resultInfo.append(idx++).append(".").append("列定义为空。\n");
            return resultInfo.toString();
        }

        int hasLabel = 0;
        for (SchemaColumn schemaColumn : columns) {
            if (schemaColumn.getIsAuth()) {
                hasLabel++;
            }
        }
        if (hasLabel != 1) {
            log.error("表{},必须有唯一名称列指定。", table.getTableName());
        }

        BizParam columnQuery = BizParam.NEW()
                .equals(ColumnDefinition.name("table_schema"), "public")
                .equals(ColumnDefinition.name("table_name"), table.getTableName());
        List<PgColumn> pgColumns;
        if (fastReturn) {
            pgColumns = pgColumnsMap.get(table.getTableName());
        } else {
            pgColumns = sqlSession.queryAll(columnQuery, PgColumn.class);
        }

        if (null == pgColumns) {
            pgColumns = new ArrayList<>();
        }

        Map<String, PgColumn> pgColumnMap = new HashMap<>();
        pgColumns.forEach(pgColumn -> pgColumnMap.put(pgColumn.getColumnName(), pgColumn));

        Map<String, SchemaColumn> columnMap = new HashMap<>();

        for (SchemaColumn schemaColumn : columns) {
            columnMap.put(schemaColumn.getColumnName(), schemaColumn);
            if (!pgColumnMap.containsKey(schemaColumn.getColumnName())) {
                if (fastReturn) return false;
                resultInfo.append(idx++).append(".").append("实体表不存在列：").append(schemaColumn.getColumnName()).append("。\n");
            }
        }

        for (PgColumn pgColumn : pgColumns) {
            if (!columnMap.containsKey(pgColumn.getColumnName())) {
                if (fastReturn) return false;
                resultInfo.append(idx++).append(".").append("实体表存在列：").append(pgColumn.getColumnName()).append("在定义中不存在。\n");
                continue;
            }
            SchemaColumn schemaColumn = columnMap.get(pgColumn.getColumnName());
            SchemaColumn.valid(schemaColumn,false);
            //
            if (schemaColumn.getIsNull() && !"YES".equals(pgColumn.getIsNullable())) {
                if (fastReturn) return false;
                resultInfo.append(idx++).append(".").append("列：").append(pgColumn.getColumnName()).append("非空选型不一致。\n");
            }
            switch (NpDataType.parseString(schemaColumn.getColumnType())) {
                case BOOL:
                    if (!"bool".equals(pgColumn.getUdtName())) {
                        if (fastReturn) return false;
                        resultInfo.append(idx++).append(".").append("列：").append(pgColumn.getColumnName()).append("类型不一致 ")
                                .append(NpDataType.ARRAY_STR.toString()).append(" VS ").append(pgColumn.getUdtName()).append("。\n");
                    }
                    break;
                case INT:
                    if (!"int4".equals(pgColumn.getUdtName())) {
                        if (fastReturn) return false;
                        resultInfo.append(idx++).append(".").append("列：").append(pgColumn.getColumnName()).append("类型不一致 ")
                                .append(NpDataType.INT.toString()).append(" VS ").append(pgColumn.getUdtName()).append("。\n");
                    }
                    break;
                case LONG:
                    if (!"int8".equals(pgColumn.getUdtName())) {
                        if (fastReturn) return false;
                        resultInfo.append(idx++).append(".").append("列：").append(pgColumn.getColumnName()).append("类型不一致 ")
                                .append(NpDataType.LONG.toString()).append(" VS ").append(pgColumn.getUdtName()).append("。\n");
                    }
                    break;
                case DECIMAL:
                    if ("numeric".equals(pgColumn.getUdtName())
                            && pgColumn.getNumericPrecision().equals(19)
                            && pgColumn.getNumericScale().equals(4)
                            ) {
                        //一致
                    } else {
                        if (fastReturn) return false;
                        resultInfo.append(idx++).append(".").append("列：").append(pgColumn.getColumnName()).append("类型不一致 ")
                                .append(NpDataType.DECIMAL.toString()).append(" VS ")
                                .append(pgColumn.getUdtName()).append(pgColumn.getNumericPrecision()).append(".").append(pgColumn.getNumericScale()).append("。\n");
                    }
                    break;
                case STR64:
                    if ("varchar".equals(pgColumn.getUdtName())
                            && pgColumn.getCharacterMaximumLength().equals(64)
                            ) {
                        //一致
                    } else {
                        if (fastReturn) return false;
                        resultInfo.append(idx++).append(".").append("列：").append(pgColumn.getColumnName()).append("类型不一致 ")
                                .append(NpDataType.STR64.toString()).append(" VS ")
                                .append(pgColumn.getUdtName()).append(pgColumn.getCharacterMaximumLength()).append("。\n");
                    }
                    break;
                case STR256:
                    if ("varchar".equals(pgColumn.getUdtName())
                            && pgColumn.getCharacterMaximumLength().equals(256)
                            ) {
                        //一致
                    } else {
                        if (fastReturn) return false;
                        resultInfo.append(idx++).append(".").append("列：").append(pgColumn.getColumnName()).append("类型不一致 ")
                                .append(NpDataType.STR256.toString()).append(" VS ")
                                .append(pgColumn.getUdtName()).append(pgColumn.getCharacterMaximumLength()).append("。\n");
                    }
                    break;
                case STR1024:
                    if ("varchar".equals(pgColumn.getUdtName())
                            && pgColumn.getCharacterMaximumLength().equals(1024)
                            ) {
                        //一致
                    } else {
                        if (fastReturn) return false;
                        resultInfo.append(idx++).append(".").append("列：").append(pgColumn.getColumnName()).append("类型不一致 ")
                                .append(NpDataType.STR1024.toString()).append(" VS ")
                                .append(pgColumn.getUdtName()).append(pgColumn.getCharacterMaximumLength()).append("。\n");
                    }
                    break;
                case TEXT:
                    if ("text".equals(pgColumn.getUdtName())) {
                        //一致
                    } else {
                        if (fastReturn) return false;
                        resultInfo.append(idx++).append(".").append("列：").append(pgColumn.getColumnName()).append("类型不一致 ")
                                .append(NpDataType.TEXT.toString()).append(" VS ")
                                .append(pgColumn.getUdtName()).append("。\n");
                    }
                    break;
                case DATE:
                    if (!"timestamp without time zone".equals(pgColumn.getDataType())) {
                        if (fastReturn) return false;
                        resultInfo.append(idx++).append(".").append("列：").append(pgColumn.getColumnName()).append("类型不一致 ")
                                .append(NpDataType.DATE.toString()).append(" VS ")
                                .append(pgColumn.getDataType()).append("。\n");
                    }
                    break;
                case ARRAY_INT:
                    if ("ARRAY".equals(pgColumn.getDataType())
                            && "_int4".equals(pgColumn.getUdtName())
                            ) {
                        //一致
                    } else {
                        if (fastReturn) return false;
                        resultInfo.append(idx++).append(".").append("列：").append(pgColumn.getColumnName()).append("类型不一致 ")
                                .append(NpDataType.ARRAY_INT.toString()).append(" VS ")
                                .append(pgColumn.getDataType()).append(pgColumn.getUdtName()).append("。\n");
                    }
                    break;
                case ARRAY_STR:
                    if ("ARRAY".equals(pgColumn.getDataType())
                            && "_text".equals(pgColumn.getUdtName())
                            ) {
                        //一致
                    } else {
                        if (fastReturn) return false;
                        resultInfo.append(idx++).append(".").append("列：").append(pgColumn.getColumnName()).append("类型不一致 ")
                                .append(NpDataType.ARRAY_STR.toString()).append(" VS ")
                                .append(pgColumn.getDataType()).append(pgColumn.getUdtName()).append("。\n");
                    }
                    break;
                case JSON:
                    if ("json".equals(pgColumn.getUdtName())) {
                        //一致
                    } else {
                        if (fastReturn) return false;
                        resultInfo.append(idx++).append(".").append("列：").append(pgColumn.getColumnName()).append("类型不一致 ")
                                .append(NpDataType.JSON.toString()).append(" VS ")
                                .append(pgColumn.getUdtName()).append("。\n");
                    }
                    break;
                default:
                    throw new NpException("未知DateType" + schemaColumn.getColumnType());
            }
        }

        //check indexes
        List<PgIndex> pgIndices;
        if (fastReturn) {
            pgIndices = pgIndexes.get(table.getTableName());
        } else {
            pgIndices = sqlSession.queryAll(tableQuery, PgIndex.class);
        }

        Set<String> pgIndexes = new HashSet<>();
        if (null != pgIndices) {
            for (PgIndex pgIndex : pgIndices) {
                pgIndexes.add(pgIndex.getIndexname());
            }
        }

        //没有主键
        String pKey = table.getTableName() + "_pkey";
        if (!pgIndexes.contains(pKey)) {
            if (fastReturn) return false;
            resultInfo.append(idx++).append(".").append("sys_id主键索引缺失。\n");
        }

        //租户唯一索引
        List<String> tenantIndex = new ArrayList<>();
        //全局唯一
        List<String> allIndex = new ArrayList<>();
        //主键约束为sys_id
        for (SchemaColumn schemaColumn : columns) {
            if (!SysColumn.sys_id.toString().equals(schemaColumn.getColumnName()) && schemaColumn.getIsAllOnly()) {
                allIndex.add(schemaColumn.getColumnName());
            }
            if (!SysColumn.tenant_id.toString().equals(schemaColumn.getColumnName()) && schemaColumn.getIsTenantOnly()) {
                tenantIndex.add(schemaColumn.getColumnName());
            }
        }

        //唯一索引缺失
        for (String allIndexTmp : allIndex) {
            if (!pgIndexes.contains(table.getTableName() + "_idx_unq_" + allIndexTmp)) {
                if (fastReturn) return false;
                resultInfo.append(idx++).append(".").append(allIndexTmp).append("全局唯一索引缺失。\n");
            }
        }

        //索引不一致
        StringBuilder columnsIdxName = new StringBuilder(table.getTableName() + "_idx_unq_");
        columnsIdxName.append(SysColumn.tenant_id.toString());
        for (String column : tenantIndex) {
            columnsIdxName.append("_").append(column);
        }

        //名称过长，PG截取索引名称!!!!!!
        //名称过长，PG截取索引名称!!!!!!
        boolean hasLian = false;
        String columnsIdxNameStr = columnsIdxName.toString();
        for (String pgIndexTmp : pgIndexes) {
            if (columnsIdxNameStr.startsWith(pgIndexTmp)) {
                hasLian = true;
            }
        }

        if (!hasLian) {
            if (fastReturn) return false;
            resultInfo.append(idx++).append(".").append(columnsIdxName).append("联合索引缺失。\n");
        }

        if (fastReturn) return true;

        return resultInfo.toString();
    }

}
