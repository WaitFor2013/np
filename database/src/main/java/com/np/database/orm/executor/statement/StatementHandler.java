package com.np.database.orm.executor.statement;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author Clinton Begin
 */
public interface StatementHandler {

    // 连接准备
    Statement prepare(Connection connection, Integer transactionTimeout) throws SQLException;

    // 参数处理
    void parameterize(Statement statement) throws SQLException;

    // CUD语句执行
    int update(Statement statement) throws SQLException;

    // 查询语句
    <E> List<E> query(Statement statement) throws SQLException;

}
