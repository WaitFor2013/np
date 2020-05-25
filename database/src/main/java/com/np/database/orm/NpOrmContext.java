package com.np.database.orm;

import com.np.database.orm.executor.Executor;
import com.np.database.orm.executor.RowBounds;
import com.np.database.orm.executor.parameter.DefaultParameterHandler;
import com.np.database.orm.executor.parameter.ParameterHandler;
import com.np.database.orm.executor.resultset.DefaultResultSetHandler;
import com.np.database.orm.executor.resultset.ResultSetHandler;
import com.np.database.orm.executor.statement.PreparedStatementHandler;
import com.np.database.orm.executor.statement.StatementHandler;
import com.np.database.orm.mapping.MappedStatement;
import com.np.database.orm.type.TypeHandlerRegistry;
import com.np.database.reflection.DefaultReflectorFactory;
import com.np.database.reflection.MetaObject;
import com.np.database.reflection.ReflectorFactory;
import com.np.database.reflection.factory.DefaultObjectFactory;
import com.np.database.reflection.factory.ObjectFactory;
import com.np.database.reflection.wrapper.DefaultObjectWrapperFactory;
import com.np.database.reflection.wrapper.ObjectWrapperFactory;
import com.np.database.orm.executor.Executor;
import com.np.database.orm.executor.RowBounds;
import com.np.database.orm.executor.parameter.DefaultParameterHandler;
import com.np.database.orm.executor.parameter.ParameterHandler;
import com.np.database.orm.executor.resultset.DefaultResultSetHandler;
import com.np.database.orm.executor.resultset.ResultSetHandler;
import com.np.database.orm.executor.statement.PreparedStatementHandler;
import com.np.database.orm.executor.statement.StatementHandler;
import com.np.database.orm.mapping.MappedStatement;
import com.np.database.orm.type.TypeHandlerRegistry;


public class NpOrmContext {

    public final static Integer transactionTimeout = 5000;

    protected final static ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
    protected final static ObjectFactory objectFactory = new DefaultObjectFactory();
    protected final static ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();
    protected final static TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();

    public static TypeHandlerRegistry getTypeHandlerRegistry() {
        return typeHandlerRegistry;
    }

    public static ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    public static ReflectorFactory getReflectorFactory() {
        return reflectorFactory;
    }

    public static MetaObject newMetaObject(Object object) {
        return MetaObject.forObject(object, objectFactory, objectWrapperFactory, reflectorFactory);
    }

    public static ObjectWrapperFactory getObjectWrapperFactory() {
        return objectWrapperFactory;
    }

    public static StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds) {
        StatementHandler statementHandler = new PreparedStatementHandler(executor, mappedStatement, parameterObject, rowBounds);
        return statementHandler;
    }

    public static ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object parameterObject) {
        ParameterHandler parameterHandler = new DefaultParameterHandler(parameterObject, mappedStatement.getBoundSql(parameterObject));
        return parameterHandler;
    }


    public static ResultSetHandler newResultSetHandler(MappedStatement mappedStatement, RowBounds rowBounds) {
        ResultSetHandler resultSetHandler = new DefaultResultSetHandler(mappedStatement, rowBounds);
        return resultSetHandler;
    }

    //init datasource
    public static NpDatasource getDatasource(NpDbConfig npDbConfig) {
        return new NpDatasource(npDbConfig);
    }

}
