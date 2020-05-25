package com.np.database.orm.executor.resultset;


import com.np.database.orm.NpOrmContext;
import com.np.database.orm.executor.ExecutorException;
import com.np.database.orm.executor.RowBounds;
import com.np.database.orm.executor.result.DefaultResultContext;
import com.np.database.orm.executor.result.DefaultResultHandler;
import com.np.database.orm.executor.result.ResultContext;
import com.np.database.orm.executor.result.ResultHandler;
import com.np.database.orm.mapping.MappedStatement;
import com.np.database.orm.mapping.ResultMap;
import com.np.database.orm.mapping.ResultMapping;
import com.np.database.orm.type.TypeHandler;
import com.np.database.orm.type.TypeHandlerRegistry;
import com.np.database.reflection.MetaClass;
import com.np.database.reflection.MetaObject;
import com.np.database.reflection.ReflectorFactory;
import com.np.database.reflection.factory.ObjectFactory;
import com.np.database.orm.executor.result.ResultHandler;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Locale;

/**
 * @author Clinton Begin
 * @author Eduardo Macarron
 * @author Iwao AVE!
 * @author Kazuki Shimizu
 */
@Slf4j
public class DefaultResultSetHandler implements ResultSetHandler {

    private static final Object DEFERRED = new Object();

    private final MappedStatement mappedStatement;
    private final RowBounds rowBounds;
    private final TypeHandlerRegistry typeHandlerRegistry;
    private final ObjectFactory objectFactory;
    private final ReflectorFactory reflectorFactory;

    public DefaultResultSetHandler(MappedStatement mappedStatement, RowBounds rowBounds) {
        this.mappedStatement = mappedStatement;
        this.rowBounds = rowBounds;
        this.typeHandlerRegistry = NpOrmContext.getTypeHandlerRegistry();
        this.objectFactory = NpOrmContext.getObjectFactory();
        this.reflectorFactory = NpOrmContext.getReflectorFactory();
    }

    /**
     * 当前仅仅支持处理单个结果集
     *
     * @param stmt
     * @return
     * @throws SQLException
     */
    @Override
    public List<Object> handleResultSets(Statement stmt) throws SQLException {

        ResultSet rs = stmt.getResultSet();
        while (rs == null) {
            // move forward to get the first resultset in case the driver
            // doesn't return the resultset as the first result (HSQLDB 2.1)
            if (stmt.getMoreResults()) {
                rs = stmt.getResultSet();
            } else {
                if (stmt.getUpdateCount() == -1) {
                    // no more results. Must be no resultset
                    break;
                }
            }
        }

        // 结果集 & 结果映射配置
        ResultSetWrapper rsw = rs != null ? new ResultSetWrapper(rs) : null;
        try {
            DefaultResultHandler defaultResultHandler = new DefaultResultHandler(objectFactory);

            // 结果渲染
            handleRowValuesForSimpleResultMap(rsw, mappedStatement.getResultMap(), defaultResultHandler, rowBounds);
            return defaultResultHandler.getResultList();

        } finally {
            // issue #228 (close resultsets)
            closeResultSet(rsw.getResultSet());
        }

    }

    private void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                //log.info("Np ResultSet closed.");
                rs.close();
            }
        } catch (SQLException e) {
            // ignore
        }
    }

    private void handleRowValuesForSimpleResultMap(ResultSetWrapper rsw, ResultMap resultMap, ResultHandler<?> resultHandler, RowBounds rowBounds)
            throws SQLException {
        DefaultResultContext<Object> resultContext = new DefaultResultContext<>();

        //结果集 游标处理
        ResultSet resultSet = rsw.getResultSet();
        skipRows(resultSet, rowBounds);

        while (shouldProcessMoreRows(resultContext, rowBounds) && !resultSet.isClosed() && resultSet.next()) {

            //获取某行对象
            Object rowValue = getRowValue(rsw, resultMap);

            //留作扩展接口，处理返回对象
            resultContext.nextResultObject(rowValue);
            ((ResultHandler<Object>) resultHandler).handleResult(resultContext);

        }
    }

    private boolean shouldProcessMoreRows(ResultContext<?> context, RowBounds rowBounds) {
        return !context.isStopped() && context.getResultCount() < rowBounds.getLimit();
    }

    private void skipRows(ResultSet rs, RowBounds rowBounds) throws SQLException {
        if (rs.getType() != ResultSet.TYPE_FORWARD_ONLY) {
            if (rowBounds.getOffset() != RowBounds.NO_ROW_OFFSET) {
                rs.absolute(rowBounds.getOffset());
            }
        } else {
            for (int i = 0; i < rowBounds.getOffset(); i++) {
                if (!rs.next()) {
                    break;
                }
            }
        }
    }

    //
    // GET VALUE FROM ROW FOR SIMPLE RESULT MAP
    //

    private Object getRowValue(ResultSetWrapper rsw, ResultMap resultMap) throws SQLException {

        //1、创建对象
        Object rowValue = createResultObject(rsw, resultMap);
        if (rowValue != null && !hasTypeHandlerForResultObject(rsw, resultMap.getType())) {
            final MetaObject metaObject = NpOrmContext.newMetaObject(rowValue);

            //2、属性映射
            applyPropertyMappings(rsw, resultMap, metaObject);
        }
        return rowValue;
    }


    //
    // PROPERTY MAPPINGS
    //

    private boolean applyPropertyMappings(ResultSetWrapper rsw, ResultMap resultMap, MetaObject metaObject)
            throws SQLException {
        final List<String> mappedColumnNames = rsw.getMappedColumnNames(resultMap);
        boolean foundValues = false;
        final List<ResultMapping> propertyMappings = resultMap.getResultMappings();
        for (ResultMapping propertyMapping : propertyMappings) {
            String column = propertyMapping.getColumn();
            if (propertyMapping.getNestedResultMapId() != null) {
                // the user added a column attribute to a nested result map, ignore it
                column = null;
            }
            if (propertyMapping.isCompositeResult()
                    || (column != null && mappedColumnNames.contains(column.toUpperCase(Locale.ENGLISH)))
                    || propertyMapping.getResultSet() != null) {
                Object value = getPropertyMappingValue(rsw.getResultSet(), propertyMapping);
                // issue #541 make param optional
                final String property = propertyMapping.getProperty();
                if (property == null) {
                    continue;
                } else if (value == DEFERRED) {
                    foundValues = true;
                    continue;
                }
                if (value != null) {
                    foundValues = true;
                }
                if (value != null || (/*configuration.isCallSettersOnNulls() && */!metaObject.getSetterType(property).isPrimitive())) {
                    // gcode issue #377, call setter on nulls (value is not 'found')
                    metaObject.setValue(property, value);
                }
            }
        }
        return foundValues;
    }

    private Object getPropertyMappingValue(ResultSet rs, ResultMapping propertyMapping)
            throws SQLException {

        final TypeHandler<?> typeHandler = propertyMapping.getTypeHandler();
        final String column = propertyMapping.getColumn();
        return typeHandler.getResult(rs, column);

    }


    //
    // INSTANTIATION & CONSTRUCTOR MAPPING
    //
    private Object createResultObject(ResultSetWrapper rsw, ResultMap resultMap) throws SQLException {
        final Class<?> resultType = resultMap.getType();
        final MetaClass metaType = MetaClass.forClass(resultType, reflectorFactory);

        if (hasTypeHandlerForResultObject(rsw, resultType)) {
            return createPrimitiveResultObject(rsw, resultMap);
        } else if (resultType.isInterface() || metaType.hasDefaultConstructor()) {
            return objectFactory.create(resultType);
        }
        throw new ExecutorException("Do not know how to create an instance of " + resultType);
    }

    private Object createPrimitiveResultObject(ResultSetWrapper rsw, ResultMap resultMap) throws SQLException {
        final Class<?> resultType = resultMap.getType();
        final String columnName;
        if (!resultMap.getResultMappings().isEmpty()) {
            final List<ResultMapping> resultMappingList = resultMap.getResultMappings();
            final ResultMapping mapping = resultMappingList.get(0);
            columnName = mapping.getColumn();
        } else {
            columnName = rsw.getColumnNames().get(0);
        }
        final TypeHandler<?> typeHandler = rsw.getTypeHandler(resultType, columnName);
        return typeHandler.getResult(rsw.getResultSet(), columnName);
    }

    private boolean hasTypeHandlerForResultObject(ResultSetWrapper rsw, Class<?> resultType) {
        if (rsw.getColumnNames().size() == 1) {
            return typeHandlerRegistry.hasTypeHandler(resultType, rsw.getJdbcType(rsw.getColumnNames().get(0)));
        }
        return typeHandlerRegistry.hasTypeHandler(resultType);
    }

}
