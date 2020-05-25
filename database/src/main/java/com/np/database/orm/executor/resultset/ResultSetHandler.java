package com.np.database.orm.executor.resultset;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author Clinton Begin
 */
public interface ResultSetHandler {

  // 返回结果集，仅仅支持单结果返回
  <E> List<E> handleResultSets(Statement stmt) throws SQLException;

}
