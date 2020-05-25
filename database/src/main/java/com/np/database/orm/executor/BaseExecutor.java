package com.np.database.orm.executor;


import com.np.database.orm.mapping.MappedStatement;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author Clinton Begin
 * <p>
 * <p>
 * modified by ch
 */
@Slf4j
public abstract class BaseExecutor implements Executor {

    protected Connection transaction;
    protected Executor wrapper;

    private boolean closed;

    protected BaseExecutor(Connection transaction) {
        this.transaction = transaction;
        this.closed = false;
        this.wrapper = this;
    }

    @Override
    public Connection getTransaction() {
        if (closed) {
            throw new ExecutorException("Executor was closed.");
        }
        return transaction;
    }

    @Override
    public void close(boolean forceRollback) {
        try {
            try {
                rollback(forceRollback);
            } finally {
                if (transaction != null) {
                    transaction.close();
                }
            }
        } catch (SQLException e) {
            // Ignore.  There's nothing that can be done at this point.
            log.warn("Unexpected exception on closing transaction.  Cause: " + e);
        } finally {
            transaction = null;
            closed = true;
        }
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public int update(MappedStatement ms, Object parameter) throws SQLException {
        if (closed) {
            throw new ExecutorException("Executor was closed.");
        }

        return doUpdate(ms, parameter);
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds) throws SQLException {
        if (closed) {
            throw new ExecutorException("Executor was closed.");
        }

        List<E> list;
        try {
            list = doQuery(ms, parameter, rowBounds);
        } finally {

        }

        return list;
    }

    @Override
    public void commit(boolean required) throws SQLException {
        if (closed) {
            throw new ExecutorException("Cannot commit, transaction is already closed");
        }

        if (required) {
            transaction.commit();
        }
    }

    @Override
    public void rollback(boolean required) throws SQLException {
        if (!closed) {
            try {

            } finally {
                if (required) {
                    transaction.rollback();
                }
            }
        }
    }

    protected abstract int doUpdate(MappedStatement ms, Object parameter) throws SQLException;

    protected abstract <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds) throws SQLException;

    protected void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                //log.info("Np statement closed.");
                statement.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    @Override
    public void setExecutorWrapper(Executor wrapper) {
        this.wrapper = wrapper;
    }

}
