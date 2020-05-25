package com.np.database.orm.executor.statement;


import com.np.database.orm.NpOrmContext;
import com.np.database.orm.executor.Executor;
import com.np.database.orm.executor.ExecutorException;
import com.np.database.orm.executor.RowBounds;
import com.np.database.orm.executor.parameter.ParameterHandler;
import com.np.database.orm.executor.resultset.ResultSetHandler;
import com.np.database.orm.mapping.BoundSql;
import com.np.database.orm.mapping.MappedStatement;
import com.np.database.orm.type.TypeHandlerRegistry;
import com.np.database.reflection.factory.ObjectFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Clinton Begin
 */
public abstract class BaseStatementHandler implements StatementHandler {

    protected final ObjectFactory objectFactory;
    protected final TypeHandlerRegistry typeHandlerRegistry;
    protected final ResultSetHandler resultSetHandler;
    protected final ParameterHandler parameterHandler;

    protected final Executor executor;
    protected final MappedStatement mappedStatement;
    protected final RowBounds rowBounds;

    protected BoundSql boundSql;

    protected BaseStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds) {
        this.executor = executor;
        this.mappedStatement = mappedStatement;
        this.rowBounds = rowBounds;

        this.typeHandlerRegistry = NpOrmContext.getTypeHandlerRegistry();
        this.objectFactory = NpOrmContext.getObjectFactory();

        this.boundSql = mappedStatement.getBoundSql(parameterObject);

        this.parameterHandler = NpOrmContext.newParameterHandler(mappedStatement, parameterObject);
        this.resultSetHandler = NpOrmContext.newResultSetHandler(mappedStatement, rowBounds);
    }

    @Override
    public Statement prepare(Connection connection, Integer transactionTimeout) throws SQLException {
        Statement statement = null;
        try {
            statement = instantiateStatement(connection);

            statement.setQueryTimeout(transactionTimeout);

            return statement;
        } catch (SQLException e) {
            closeStatement(statement);
            throw e;
        } catch (Exception e) {
            closeStatement(statement);
            throw new ExecutorException("Error preparing statement.  Cause: " + e, e);
        }
    }

    protected abstract Statement instantiateStatement(Connection connection) throws SQLException;

    protected void closeStatement(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            //ignore
        }
    }


}
