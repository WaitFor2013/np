package com.np.database.orm.session;


import com.np.database.*;
import com.np.database.exception.NpDbException;
import com.np.database.orm.NpOrmContext;
import com.np.database.orm.SerialId;
import com.np.database.orm.SqlCommandType;
import com.np.database.orm.TablePrefix;
import com.np.database.orm.biz.BizSqlRender;
import com.np.database.orm.biz.BizSqlSource;
import com.np.database.orm.biz.DbTypeEnum;
import com.np.database.orm.biz.param.BizParam;
import com.np.database.orm.biz.param.BizProperty;
import com.np.database.orm.executor.Executor;
import com.np.database.orm.executor.RowBounds;
import com.np.database.orm.executor.SimpleExecutor;
import com.np.database.orm.mapping.*;
import com.np.database.orm.type.JdbcType;
import com.np.database.reflection.MetaObject;
import com.np.database.orm.executor.Executor;
import com.np.database.orm.executor.RowBounds;
import com.np.database.orm.executor.SimpleExecutor;
import com.np.database.orm.mapping.ColumnMapping;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class DefaultSqlSession implements SqlSession {

    private final Executor executor;
    private final Connection connection;
    private final boolean autoCommit;
    private final DbTypeEnum dbTypeEnum;

    public DefaultSqlSession(DataSource dataSource, DbTypeEnum dbTypeEnum, boolean autoCommit) {

        try {
            Connection connection = dataSource.getConnection();
            connection.setAutoCommit(autoCommit);

            this.dbTypeEnum = dbTypeEnum;
            this.connection = connection;
            this.autoCommit = autoCommit;
            this.executor = new SimpleExecutor(connection);
        } catch (SQLException e) {
            throw new NpDbException(e);
        }
    }

    @Override
    public <E> List<E> queryAll(BizParam parameter, Class<E> resultType) {
        if (null == parameter || null == resultType) {
            throw new NpDbException("parameter and resultType can't be null.");
        }

        TableMeta tableMeta = generateTableMeta(resultType, true, false, SqlCommandType.SELECT);
        String simpleSelectSql = BizSqlRender.renderSimpleSelect(tableMeta.tableName, tableMeta.allColumns);

        return query(simpleSelectSql, parameter, resultType);
    }

    @Override
    public int count(BizParam parameter, Class resultType) {
        if (null == parameter || null == resultType) {
            throw new NpDbException("parameter and resultType can't be null.");
        }

        TableMeta tableMeta = generateTableMeta(resultType, true, false, SqlCommandType.SELECT);
        String simpleSelectSql = BizSqlRender.renderSimpleSelect(tableMeta.tableName, tableMeta.allColumns);

        return count(simpleSelectSql, parameter, resultType);
    }

    //@Override
    private <E> List<E> query(String sql, BizParam parameter, Class<E> resultType) {
        if (null == sql || null == parameter || null == resultType) {
            throw new NpDbException("sql and parameter and resultType can't be null.");
        }

        TableMeta tableMeta = generateTableMeta(resultType, false, false, SqlCommandType.SELECT);

        MappedStatement mappedStatement = prepareSelectOneMappedStatement(sql, resultType, tableMeta.allColumns);
        try {
            return executor.query(mappedStatement, parameter, new RowBounds());
        } catch (SQLException e) {
            log.error("query failed", e);
            throw new NpDbException("query failed:" + e.getMessage(), e);
        }
    }

    @Override
    public <E extends NoRepeatView> List<E> queryView(BizParam param, Class<E> resultType) {

        ExtView extView = resultType.getAnnotation(ExtView.class);

        if (null != extView) {
            String noWhereSql = NpViewDefinition.getNoWhereSql(extView.viewName());

            if (null == noWhereSql) {
                throw new NpDbException(extView.viewComment() + "：没有成功注册。");
            }

            return query(noWhereSql, param, resultType);
        }

        return null;
    }

    @Override
    public <E extends NoRepeatView> int countView(BizParam param, Class<E> resultType) {

        ExtView extView = resultType.getAnnotation(ExtView.class);

        if (null != extView) {
            String noWhereSql = NpViewDefinition.getNoWhereSql(extView.viewName());

            if (null == noWhereSql) {
                throw new NpDbException(extView.viewComment() + "：没有成功注册。");
            }

            return count(noWhereSql, param, resultType);
        }

        return 0;
    }

    //@Override
    public int count(String sql, BizParam parameter, Class resultType) {
        if (null == sql || null == parameter || null == resultType) {
            throw new NpDbException("sql and parameter and resultType can't be null.");
        }

        TableMeta tableMeta = generateTableMeta(resultType, false, false, SqlCommandType.SELECT);

        BizSqlSource bizSqlSource = new BizSqlSource(dbTypeEnum, tableMeta.allColumns);
        bizSqlSource.initCount(sql);

        ResultMap rm = new ResultMap.Builder(Integer.class, new ArrayList<>()).build();
        MappedStatement mappedStatement = new MappedStatement.Builder(bizSqlSource, SqlCommandType.SELECT).resultMap(rm).build();
        try {
            List<Object> query = executor.query(mappedStatement, parameter, new RowBounds());
            if (null == query || query.size() != 1) {
                log.error("Internal count error,count should return just one!");
                return 0;
            }
            Object size = query.get(0);
            if (size instanceof Integer) {
                return (Integer) size;
            } else {
                log.error("Internal count error,result should be Integer!");
                return 0;
            }
        } catch (SQLException e) {
            log.error("count failed", e);
            throw new NpDbException("count failed:" + e.getMessage(), e);
        }
    }

    public List<Map> query(String sql, BizParam parameter, List<ColumnMapping> allColumns) {
        if (null == sql || null == parameter || null == allColumns) {
            throw new NpDbException("sql and parameter and allParams can't be null.");
        }

        MappedStatement mappedStatement = prepareSelectOneMappedStatement(sql, Map.class, allColumns);
        try {
            return executor.query(mappedStatement, parameter, new RowBounds());
        } catch (SQLException e) {
            log.error("query failed", e);
            throw new NpDbException("query failed:" + e.getMessage(), e);
        }
    }

    private MappedStatement prepareSelectOneMappedStatement(String sql, Class resultType, List<ColumnMapping> allColumns) {
        ArrayList<ResultMapping> resultMappings = new ArrayList<>();
        for (ColumnMapping parameterMapping : allColumns) {
            if (null != parameterMapping.getPojoProperty()) {
                ResultMapping buildRst = new ResultMapping.Builder(parameterMapping.getPojoProperty(), parameterMapping.getColumn(), parameterMapping.getTypeHandler()).build();
                resultMappings.add(buildRst);
            }
        }

        BizSqlSource bizSqlSource = new BizSqlSource(dbTypeEnum, allColumns);

        bizSqlSource.initSelect(sql);


        ResultMap rm = new ResultMap.Builder(resultType, resultMappings).build();
        return new MappedStatement.Builder(bizSqlSource, SqlCommandType.SELECT).resultMap(rm).build();
    }

    @Override
    public <T> T queryById(T parameter) {
        if (null == parameter) {
            throw new NpDbException("parameter can't be null.");
        }

        TableMeta tableMeta = generateTableMeta(parameter.getClass(), true, true, SqlCommandType.SELECT);

        MetaObject metaObject = MetaObject.forObject(parameter, NpOrmContext.getObjectFactory(), NpOrmContext.getObjectWrapperFactory(), NpOrmContext.getReflectorFactory());

        Object idValue = metaObject.getValue(tableMeta.idColumn.getPojoProperty());
        if (null == idValue || idValue.toString().isEmpty()) {
            throw new NpDbException("ID value can't be null.");
        }

        BizParam idParam = BizParam.NEW().index(ColumnDefinition.name(tableMeta.idColumn.getColumn()), idValue);

        String simpleSelectSql = BizSqlRender.renderSimpleSelect(tableMeta.tableName, tableMeta.allColumns);

        MappedStatement mappedStatement = prepareSelectOneMappedStatement(simpleSelectSql, parameter.getClass(), tableMeta.allColumns);
        try {
            List<T> query = executor.query(mappedStatement, idParam, new RowBounds());
            if (null == query || query.isEmpty()) {
                return null;
            } else if (query.size() > 1) {
                throw new NpDbException("FindById return more than one row.");
            } else {
                return query.get(0);
            }

        } catch (SQLException e) {
            log.error("query failed", e);
            throw new NpDbException("query failed:" + e.getMessage(), e);
        }

    }


    private TableMeta generateTableMeta(Class<?> parameterClass, boolean checkTable, boolean checkId, SqlCommandType sqlCommandType) {
        TableMeta tableMeta = new TableMeta();

        if (checkTable) {
            Table tableAnnotation = parameterClass.getAnnotation(Table.class);
            if (null == tableAnnotation || tableAnnotation.name().isEmpty()) {
                throw new NpDbException("Object must have Table annotation and can't be empty.");
            }
            tableMeta.tableName = tableAnnotation.name();
        }

        List<ColumnMapping> allColumns = new ArrayList<>();

        Field[] declaredFields = parameterClass.getDeclaredFields();
        ColumnMapping idFiled = null;

        if (null != declaredFields) {
            for (Field field : declaredFields) {
                Id idAnnotation = field.getAnnotation(Id.class);
                Column columnAnnotation = field.getAnnotation(Column.class);
                TablePrefix tablePrefix = field.getAnnotation(TablePrefix.class);
                SerialId serialId = field.getAnnotation(SerialId.class);

                if (sqlCommandType.equals(SqlCommandType.INSERT)) {
                    //自增列不插入
                    if (null != serialId)
                        continue;
                }

                if (null != columnAnnotation && !columnAnnotation.name().isEmpty()) {
                    ColumnMapping.Builder builder = new ColumnMapping.Builder(columnAnnotation.name(), field.getType())
                            .pojoProperty(field.getName());

                    if (List.class.isAssignableFrom(field.getType())) {
                        Type rawType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                        if ("java.lang.String".equals(rawType.getTypeName())) {
                            builder.jdbcType(JdbcType.VARCHAR);
                        } else if ("java.lang.Integer".equals(rawType.getTypeName())) {
                            builder.jdbcType(JdbcType.INTEGER);
                        } else {
                            throw new NpDbException("数组只支持Integer or String");
                        }
                    }

                    if (null != tablePrefix && !tablePrefix.value().isEmpty()) {
                        builder.columnPrefix(tablePrefix.value());
                    }

                    ColumnMapping build = builder.build();

                    allColumns.add(build);

                    if (null != idAnnotation) {
                        if (null != idFiled) {
                            throw new NpDbException("Object must just has one Id annotation");
                        }
                        idFiled = build;
                    }
                }
            }
        }

        if (allColumns.isEmpty()) {
            //throw new NpDbException("Object columns can't be null");
        }

        //view extend
        if (NoRepeatView.class.isAssignableFrom(parameterClass)) {
            ExtView extView = parameterClass.getAnnotation(ExtView.class);
            List<NpViewDefinition.ViewQueryParam> queryParams = NpViewDefinition.getQueryParams(extView.viewName());

            if (null != queryParams && !queryParams.isEmpty()) {
                for (int z = 0; z < queryParams.size(); z++) {
                    NpViewDefinition.ViewQueryParam viewQueryParam = queryParams.get(z);

                    String columnName = viewQueryParam.getTableName() + "$" + viewQueryParam.getColumnName();

                    //columnName = NpOrmContext.toCamel(columnName, false);

                    ColumnMapping.Builder builder = new ColumnMapping.Builder(columnName, NpDataType.getFromTypeClazz(viewQueryParam.getColumnType()));
                    if (NpDataType.ARRAY_INT.toString().equals(viewQueryParam.getColumnType())) {
                        builder.jdbcType(JdbcType.INTEGER);
                    }
                    if (NpDataType.ARRAY_STR.toString().equals(viewQueryParam.getColumnType())) {
                        builder.jdbcType(JdbcType.VARCHAR);
                    }

                    if (null != viewQueryParam.getTablePrefix() && !viewQueryParam.getTablePrefix().isEmpty()) {
                        builder.columnPrefix(viewQueryParam.getTablePrefix());
                    }
                    allColumns.add(builder.build());
                }
            }
        }

        tableMeta.allColumns = allColumns;

        if (null == idFiled && checkId) {
            throw new NpDbException("Object must just has one Id annotation");
        } else {
            tableMeta.idColumn = idFiled;
        }


        return tableMeta;
    }

    @Override
    public int create(Object parameter) {

        return doInternal(parameter, SqlCommandType.INSERT, false, null, "create");
    }

    @Override
    public int updateById(Object parameter) {
        return doInternal(parameter, SqlCommandType.UPDATE, true, null, "updateById");
    }

    @Override
    public int deleteById(Object parameter) {
        return doInternal(parameter, SqlCommandType.DELETE, true, null, "updateById");
    }

    private Set<String> fromColumnDef(ColumnDefinition... columnDes) {
        Set<String> columns = new HashSet<>();
        for (ColumnDefinition columnDefinition : columnDes) {
            columns.add(columnDefinition.getColumnName());
        }
        return columns;
    }

    @Override
    public int updateByColumns(Object parameter, ColumnDefinition... columnDes) {
        if (null == columnDes || columnDes.length == 0) {
            throw new NpDbException("columns can't be null or empty.");
        }
        return doInternal(parameter, SqlCommandType.UPDATE, false, fromColumnDef(columnDes), "updateByColumns");
    }

    @Override
    public int deleteByColumns(Object parameter, ColumnDefinition... columnDes) {
        if (null == columnDes || columnDes.length == 0) {
            throw new NpDbException("columns can't be null or empty.");
        }
        return doInternal(parameter, SqlCommandType.DELETE, false, fromColumnDef(columnDes), "deleteByColumns");
    }

    //DDL execute
    public int executeDDL(String sql) {
        StaticSqlSource staticSqlSource = new StaticSqlSource(sql);
        MappedStatement build = new MappedStatement.Builder(staticSqlSource, SqlCommandType.UPDATE).build();
        try {
            return executor.update(build, null);
        } catch (SQLException e) {
            log.error("executeDDL failed", e);
            throw new NpDbException("executeDDL failed:" + e.getMessage(), e);
        }
    }

    private BizParam objectMapping(Object parameter, TableMeta tableMeta, Set<String> indexed) {
        List<BizProperty> list = new ArrayList<>();
        MetaObject metaObject = MetaObject.forObject(parameter, NpOrmContext.getObjectFactory(), NpOrmContext.getObjectWrapperFactory(), NpOrmContext.getReflectorFactory());

        for (ColumnMapping columnMapping : tableMeta.allColumns) {
            Object value = metaObject.getValue(columnMapping.getPojoProperty());
            if (null == value) {
                //throw new NpDbException("column value can't be null when insert:" + columnMapping.getPojoProperty());
            }
            BizProperty tmpProperty = BizProperty.builder().column(columnMapping.getColumn()).values(new Object[]{value}).build();

            if (indexed.contains(columnMapping.getPojoProperty())) {
                //set indexed
                tmpProperty.setIsIndex(true);
            }

            list.add(tmpProperty);
        }

        BizParam bizParam = BizParam.NEW();
        bizParam.setProperties(list.toArray(new BizProperty[]{}));

        return bizParam;

    }

    private int doInternal(Object parameter, SqlCommandType sqlCommandType, boolean checkId, Set<String> columns, String methodName) {
        if (null == parameter) {
            throw new NpDbException("parameter can't be null.");
        }

        TableMeta tableMeta = generateTableMeta(parameter.getClass(), true, checkId, sqlCommandType);

        BizSqlSource bizSqlSource = new BizSqlSource(dbTypeEnum, tableMeta.allColumns);
        switch (sqlCommandType) {
            case INSERT:
                bizSqlSource.initInsert(tableMeta.tableName);
                break;
            case UPDATE:
                bizSqlSource.initUpdate(tableMeta.tableName);
                break;
            case DELETE:
                bizSqlSource.initDelete(tableMeta.tableName);
                break;
            default:
                throw new NpDbException("unsupported sqlCommandType:" + bizSqlSource);
        }

        MappedStatement build = new MappedStatement.Builder(bizSqlSource, SqlCommandType.INSERT).build();

        Set<String> indexed = new HashSet<>();
        if (checkId) {
            indexed.add(tableMeta.idColumn.getPojoProperty());
        }
        if (null != columns) {
            Map<String, ColumnMapping> columnMappingMap = new HashMap<>();
            tableMeta.allColumns.forEach(columnMapping -> columnMappingMap.put(columnMapping.getColumn(), columnMapping));

            for (String column : columns) {
                if (columnMappingMap.containsKey(column)) {
                    indexed.add(columnMappingMap.get(column).getPojoProperty());
                } else {
                    throw new NpDbException("Object dons't has column:" + column);
                }
            }
        }

        BizParam bizParam = objectMapping(parameter, tableMeta, indexed);

        try {
            return executor.update(build, bizParam);
        } catch (SQLException e) {
            log.error(methodName + " failed", e);
            throw new NpDbException(methodName + " failed:" + e.getMessage(), e);
        }
    }

    @Override
    public void commit() {
        try {
            executor.commit(true);
        } catch (SQLException e) {
            throw new NpDbException("commit failed:" + e.getMessage(), e);
        }
    }

    @Override
    public void rollback() {
        try {
            executor.rollback(true);
        } catch (SQLException e) {
            throw new NpDbException("rollback failed:" + e.getMessage(), e);
        }
    }

    @Override
    public void close() throws Exception {
        executor.close(!autoCommit);
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    private static class TableMeta {
        String tableName;
        ColumnMapping idColumn;
        List<ColumnMapping> allColumns;
    }
}
