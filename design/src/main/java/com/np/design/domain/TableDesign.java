package com.np.design.domain;

import com.alibaba.fastjson.JSON;
import com.np.database.ColumnDefinition;
import com.np.database.NpDataType;
import com.np.database.orm.NpDatasource;
import com.np.database.orm.biz.param.BizParam;
import com.np.database.orm.biz.param.Direction;
import com.np.database.orm.session.DefaultSqlSession;
import com.np.database.orm.session.SqlSession;
import com.np.database.recommend.SysColumn;
import com.np.design.domain.db.SchemaColumn;
import com.np.design.domain.db.SchemaTable;
import com.np.design.domain.ddl.*;
import com.np.design.domain.misc.GridHelper;
import com.np.design.exception.NpException;
import com.np.design.domain.db.SchemaColumn;
import com.np.design.domain.db.SchemaTable;
import com.np.design.domain.ddl.*;
import com.np.design.domain.misc.GridHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.np.design.domain.db.SchemaColumn.C_COLUMN_NAME;
import static com.np.design.domain.db.SchemaColumn.C_TABLE_NAME;

@Slf4j
public class TableDesign {

    private static final AtomicBoolean dbLoaded = new AtomicBoolean(false);
    private static final List<SchemaTable> _dbTables = new ArrayList<>();
    private static final Map<String, List<SchemaColumn>> _dbColumns = new HashMap<>();

    private static final List<SchemaTable> tablesCache = new ArrayList<>();
    private static final Map<String, List<SchemaColumn>> columnsCache = new HashMap<>();

    public static SchemaColumn getDbColumn(String tableName, String columnName) {
        if (_dbColumns.containsKey(tableName)) {
            List<SchemaColumn> schemaColumns = _dbColumns.get(tableName);
            for (SchemaColumn temp : schemaColumns) {
                if (temp.getColumnName().equals(columnName)) {
                    return temp;
                }
            }
        }
        return null;
    }

    //get columns
    public static String[] otherTables() {
        String[] otherTables = new String[_dbTables.size() + 2];
        otherTables[0] = "N";
        otherTables[1] = "SELF";
        for (int i = 0; i < _dbTables.size(); i++) {
            otherTables[2 + i] = _dbTables.get(i).getTableName();
        }
        return otherTables;
    }

    public static boolean hasTableInDb(String tableName) {
        return null != SchemaTable.contains(_dbTables, tableName);
    }

    public static boolean hasTableInCache(String tableName) {
        int size = 0;
        for (SchemaTable table : tablesCache) {
            if (null != table.getTableName() && tableName.equals(table.getTableName())) {
                size++;
            }
        }
        return size > 0 ? true : false;
    }

    public static boolean isDbLoad() {
        return dbLoaded.get();
    }

    private static void check(SchemaTable schemaTable, List<SchemaColumn> columns) {

        int countTableName = 0, countAbbr = 0;
        for (SchemaTable tableTmp : tablesCache) {

            if (null != tableTmp.getTableName() && tableTmp.getTableName().equals(schemaTable.getTableName())) {
                countTableName++;
            }
            if (null != tableTmp.getTableAbbr() && tableTmp.getTableAbbr().equals(schemaTable.getTableAbbr())) {
                countAbbr++;
            }
        }
        if (countTableName > 1) {
            throw new NpException("表名重复：" + schemaTable.getTableName());
        }
        if (countAbbr > 1) {
            throw new NpException("表缩写重复：" + schemaTable.getTableAbbr());
        }


        Set<String> hash = new HashSet<>();
        for (SchemaColumn schemaColumn : columns) {
            if (hash.contains(schemaColumn.getColumnName())) {
                throw new NpException("列名重复：" + schemaColumn.getColumnName());
            }
            hash.add(schemaColumn.getColumnName());
        }
    }

    //增量发布
    public static void alterDeploy(SchemaTable schemaTable, List<SchemaColumn> columns) {
        check(schemaTable, columns);

        DefaultSqlSession session = null;
        try {
            NpDatasource datasource = NpDataBaseCache.getDatasource();
            session = (DefaultSqlSession) datasource.getSession(false);
            //deploy
            SchemaTable dbTable = SchemaTable.contains(_dbTables, schemaTable.getTableName());
            if (null != dbTable) {

                //List<SchemaColumn> dbColumns = _dbColumns.get(dbTable.getTableName());

                Map<String, PgColumn> dbSchemaMap = new HashMap<>();

                BizParam columnQuery = BizParam.NEW()
                        .equals(ColumnDefinition.name("table_schema"), "public")
                        .equals(ColumnDefinition.name("table_name"), schemaTable.getTableName());
                List<PgColumn> pgColumns = session.queryAll(columnQuery, PgColumn.class);

                pgColumns.forEach(dbTemp -> dbSchemaMap.put(dbTemp.getColumnName(), dbTemp));

                int count = dbSchemaMap.size();
                for (int i = 0; i < columns.size(); i++) {
                    SchemaColumn xAdd = columns.get(i);
                    if (dbSchemaMap.containsKey(xAdd.getColumnName())) {

                       /* PgColumn oldColumn = dbSchemaMap.get(xAdd.getColumnName());
                        if (!xAdd.getColumnType().equals(oldColumn.getColumnType())
                                || !xAdd.getIsNull().equals(oldColumn.getIsNull())) {
                            throw new NpException("增量发布，列类型和非空不允许修改。");
                        }
*/
                        count--;
                    } else {
                        if (xAdd.getIsNull()) {

                            //增加列
                            //ALTER TABLE table_name ADD column_name datatype;
                            String pgType = PostgresDDLHelper.pgType(NpDataType.parseString(xAdd.getColumnType()));
                            session.executeDDL("ALTER TABLE " + schemaTable.getTableName() + " ADD " + xAdd.getColumnName() + " " + pgType);

                        } else {
                            throw new NpException("增量发布，只允许增加可空列:" + xAdd.getColumnName());
                        }
                    }

                }

                if (count != 0) {
                    throw new NpException("增量发布，只允许增加可空列，不可删除列。");
                }


                //删除表记录&删除列记录
                session.deleteById(dbTable);
                SchemaColumn deleteColumnQuery = SchemaColumn.builder().tableName(dbTable.getTableName()).build();
                session.deleteByColumns(deleteColumnQuery, ColumnDefinition.name(SchemaColumn.C_TABLE_NAME));
                //need commit
                session.commit();


                //save table and columns
                session.create(schemaTable);
                for (SchemaColumn tempC : columns) {
                    session.create(tempC);
                }

                //add to local db cache
                Iterator<SchemaTable> iterator = _dbTables.iterator();
                while (iterator.hasNext()) {
                    SchemaTable next = iterator.next();
                    if (next.getTableName().equals(dbTable.getTableName())) {
                        iterator.remove();
                    }
                }
                _dbTables.add(GridHelper.copyFromTo(schemaTable, new SchemaTable()));

                //add to local column cache
                List<SchemaColumn> _columnsList = new ArrayList<>();
                for (SchemaColumn schemaColumn : columns) {
                    _columnsList.add(GridHelper.copyFromTo(schemaColumn, new SchemaColumn()));
                }
                _dbColumns.remove(schemaTable.getTableName());
                _dbColumns.put(schemaTable.getTableName(), _columnsList);

                session.commit();

            } else {

                //
                throw new NpException("表未发布过，无须进行增量发布");
            }


        } finally {
            if (null != session) {
                try {
                    session.close();
                } catch (Exception ex) {
                    //do nothing
                }
            }
        }
    }

    public static void deploy(SchemaTable schemaTable, List<SchemaColumn> columns) {

        check(schemaTable, columns);

        DefaultSqlSession session = null;
        try {
            NpDatasource datasource = NpDataBaseCache.getDatasource();
            session = (DefaultSqlSession) datasource.getSession(false);
            //deploy
            SchemaTable dbTable = SchemaTable.contains(_dbTables, schemaTable.getTableName());
            if (null != dbTable) {

                log.info("进入此判断***********************");

                List<PgTable> pgTables = session.queryAll(BizParam.NEW().notIn(ColumnDefinition.name("schemaname"), new Object[]{"pg_catalog", "information_schema", "sys"}), PgTable.class);
                if (PgTable.contains(pgTables, dbTable.getTableName())) {
                    int count = session.count("select count(*) from " + schemaTable.getTableName(), BizParam.NEW(), Map.class);
                    if (count > 0) {
                        throw new NpException("表中有数据，备份数据后在进行发布。");
                    }

                    //删除表
                    session.executeDDL("DROP TABLE " + dbTable.getTableName());

                    //删除表索引
                    BizParam tableQuery = BizParam.NEW()
                            .equals(ColumnDefinition.name("schemaname"), "public")
                            .equals(ColumnDefinition.name("tablename"), dbTable.getTableName());
                    List<PgIndex> pgIndices = session.queryAll(tableQuery, PgIndex.class);
                    log.info("索引信息{}", JSON.toJSON(pgIndices));
                    if (null != pgIndices) {
                        for (PgIndex pgIndex : pgIndices) {
                            session.executeDDL(" DROP INDEX " + pgIndex.getIndexname());
                        }
                    }
                }

                //删除表记录&删除列记录
                session.deleteById(dbTable);

                SchemaColumn deleteColumnQuery = SchemaColumn.builder().tableName(dbTable.getTableName()).build();
                session.deleteByColumns(deleteColumnQuery, ColumnDefinition.name(SchemaColumn.C_TABLE_NAME));
                //need commit
                session.commit();

                Iterator<SchemaTable> iterator = _dbTables.iterator();
                while (iterator.hasNext()) {
                    SchemaTable next = iterator.next();
                    if (next.getTableName().equals(dbTable.getTableName())) {
                        iterator.remove();
                    }
                }

            }

            //租户唯一索引
            List<String> tenantIndex = new ArrayList<>();

            //全局唯一
            List<String> allIndex = new ArrayList<>();

            //主键约束为sys_id
            //save table and columns
            session.create(schemaTable);
            for (SchemaColumn schemaColumn : columns) {
                session.create(schemaColumn);

                if (!SysColumn.sys_id.toString().equals(schemaColumn.getColumnName()) && schemaColumn.getIsAllOnly()) {
                    allIndex.add(schemaColumn.getColumnName());
                }

                if (!SysColumn.tenant_id.toString().equals(schemaColumn.getColumnName()) && schemaColumn.getIsTenantOnly()) {
                    tenantIndex.add(schemaColumn.getColumnName());
                }

            }

            PostgresDDLHelper.createSql(session, schemaTable, columns);

            if (tenantIndex.isEmpty()) {
                throw new NpException("必须制定租户唯一索引");
            }

            //create index
            PostgresDDLHelper.createIndex(session, schemaTable, tenantIndex, allIndex);


            //add to local db cache
            _dbTables.add(GridHelper.copyFromTo(schemaTable, new SchemaTable()));

            //add to local column cache
            List<SchemaColumn> _columnsList = new ArrayList<>();
            for (SchemaColumn schemaColumn : columns) {
                _columnsList.add(GridHelper.copyFromTo(schemaColumn, new SchemaColumn()));
            }
            _dbColumns.remove(schemaTable.getTableName());
            _dbColumns.put(schemaTable.getTableName(), _columnsList);

            session.commit();

        } finally {
            if (null != session) {
                try {
                    session.close();
                } catch (Exception ex) {
                    //do nothing
                }
            }
        }
    }

    public static void tableNameChange(String from, String to) {
        List<SchemaColumn> list = columnsCache.get(from);
        columnsCache.put(to, list);
        columnsCache.remove(from);
    }

    public static void removeColumns(String tableName, Boolean isInDatabase) {
        columnsCache.remove(tableName);
        if (isInDatabase) {

            DefaultSqlSession session = null;
            try {
                NpDatasource datasource = NpDataBaseCache.getDatasource();
                session = (DefaultSqlSession) datasource.getSession(false);

                Iterator<SchemaTable> tableIterator = _dbTables.iterator();
                while (tableIterator.hasNext()) {
                    SchemaTable schemaTable = tableIterator.next();
                    if (tableName.equals(schemaTable.getTableName())) {
                        //remove db record
                        session.deleteById(schemaTable);
                        tableIterator.remove();
                    }
                }

                List<SchemaColumn> list = _dbColumns.get(tableName);
                if (null != list) {
                    for (SchemaColumn schemaColumn : list) {
                        session.deleteByColumns(schemaColumn, ColumnDefinition.name(SchemaColumn.C_TABLE_NAME), ColumnDefinition.name(SchemaColumn.C_COLUMN_NAME));
                    }
                }

                _dbColumns.remove(tableName);

                session.executeDDL("drop table if exists " + tableName);
                session.commit();


            } finally {
                if (null != session) {
                    try {
                        session.close();
                    } catch (Exception ex) {
                        //do nothing
                    }
                }
            }
        }
    }

    public static List<SchemaColumn> getColumns(String tableName) {
        List<SchemaColumn> schemaColumns = columnsCache.get(tableName);
        if (null == schemaColumns) {
            List<SchemaColumn> initColumns = GridHelper.initColumns(tableName);
            columnsCache.put(tableName, initColumns);
        }
        return columnsCache.get(tableName);
    }

    public static void saveTables(String[] header, Object[][] data) {
        if (null == data || data.length == 0) {
            tablesCache.clear();
            return;
        } else {
            List<SchemaTable> temp = new ArrayList<>();
            for (int i = 0; i < data.length; i++) {
                //do carefully
                temp.add(SchemaTable.newInstance(header, data[i]));
            }
            tablesCache.clear();
            temp.forEach(schemaTable -> tablesCache.add(schemaTable));
        }
    }

    public static void saveColumns(String tableName, String[] header, Object[][] data) {

        List<SchemaColumn> list = new ArrayList<>();
        if (null != data && data.length != 0) {
            for (int i = 0; i < data.length; i++) {
                SchemaColumn schemaColumn = SchemaColumn.newInstance(header, data[i]);
                schemaColumn.setTableName(tableName);
                list.add(schemaColumn);
            }
        }
        columnsCache.put(tableName, list);
    }

    public static List<SchemaTable> loadDb(boolean isViewUpdate) {
        //clear all
        dbLoaded.set(false);
        _dbTables.clear();
        _dbColumns.clear();
        tablesCache.clear();
        columnsCache.clear();


        SqlSession session = null;
        try {
            NpDatasource datasource = NpDataBaseCache.getDatasource();
            session = datasource.getSession(true);

            PgConsistencyCheck.init(session);

            List<SchemaTable> schemaTables = session.queryAll(BizParam.NEW().orderBy(ColumnDefinition.name("id"), Direction.ASC), SchemaTable.class);

            //get columns
            List<SchemaColumn> allColumns = session.queryAll(
                    BizParam.NEW()
                            .orderBy(ColumnDefinition.name("id"), Direction.ASC)
                    , SchemaColumn.class);

            Map<String, List<SchemaColumn>> columnsMap = new HashMap<>();
            if (null != allColumns) {
                for (SchemaColumn schemaColumn : allColumns) {
                    List<SchemaColumn> schemaColumns = null;
                    if (columnsMap.containsKey(schemaColumn.getTableName())) {
                        schemaColumns = columnsMap.get(schemaColumn.getTableName());
                    } else {
                        schemaColumns = new ArrayList<>();
                        columnsMap.put(schemaColumn.getTableName(), schemaColumns);
                    }
                    schemaColumns.add(schemaColumn);
                }
            }

            if (null != schemaTables && !schemaTables.isEmpty()) {
                for (int i = 0; i < schemaTables.size(); i++) {
                    //get table
                    SchemaTable dbSchemaTable = schemaTables.get(i);
                    List<SchemaColumn> columns = columnsMap.get(dbSchemaTable.getTableName());

                    if (PgConsistencyCheck.doCheck(session, dbSchemaTable, columns)) {
                        dbSchemaTable.setIsRelease(true);
                    }

                    //log.info("导入数据库，{}", dbSchemaTable.getTableName());

                    _dbTables.add(dbSchemaTable);

                    SchemaTable newObj = new SchemaTable();
                    GridHelper.copyFromTo(dbSchemaTable, newObj);
                    tablesCache.add(newObj);

                    //copy columns
                    if (null != columns && !columns.isEmpty()) {
                        _dbColumns.put(newObj.getTableName(), columns);
                        List<SchemaColumn> newCopyList = new ArrayList<>();
                        for (SchemaColumn schemaColumn : columns) {
                            SchemaColumn newColumn = new SchemaColumn();
                            GridHelper.copyFromTo(schemaColumn, newColumn);
                            newCopyList.add(newColumn);
                        }
                        columnsCache.put(newObj.getTableName(), newCopyList);
                    }
                }
            }
            if (isViewUpdate) {
                dbLoaded.set(true);
            }
            return tablesCache;

        } finally {
            if (null != session) {
                try {
                    session.close();
                } catch (Exception ex) {
                    //do nothing
                }
            }
        }
    }

    public static List<SchemaTable> getTables() {
        return tablesCache;
    }
}
