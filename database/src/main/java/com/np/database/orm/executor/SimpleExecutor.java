package com.np.database.orm.executor;


import com.np.database.orm.NpOrmContext;
import com.np.database.orm.executor.statement.StatementHandler;
import com.np.database.orm.mapping.MappedStatement;
import com.np.database.orm.executor.statement.StatementHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author Clinton Begin
 */
public class SimpleExecutor extends BaseExecutor {

  public SimpleExecutor(Connection transaction) {
    super(transaction);
  }

  @Override
  public int doUpdate(MappedStatement ms, Object parameter) throws SQLException {
    Statement stmt = null;
    try {
      StatementHandler handler = NpOrmContext.newStatementHandler(this, ms, parameter, RowBounds.DEFAULT);
      stmt = prepareStatement(handler);
      return handler.update(stmt);
    } finally {
      closeStatement(stmt);
    }
  }

  @Override
  public <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds) throws SQLException {
    Statement stmt = null;
    try {

      StatementHandler handler = NpOrmContext.newStatementHandler(wrapper, ms, parameter, rowBounds);
      stmt = prepareStatement(handler);
      return handler.query(stmt);
    } finally {
      closeStatement(stmt);
    }
  }


  private Statement prepareStatement(StatementHandler handler) throws SQLException {
    Statement stmt;
    Connection connection = getTransaction();
    stmt = handler.prepare(connection, NpOrmContext.transactionTimeout);
    handler.parameterize(stmt);
    return stmt;
  }

}
