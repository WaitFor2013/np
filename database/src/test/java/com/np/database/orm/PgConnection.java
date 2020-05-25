package com.np.database.orm;

import com.np.database.sql.util.JdbcUtils;
import com.np.database.sql.util.JdbcUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


@Slf4j
public class PgConnection {

    private static String url = "jdbc:postgresql://127.0.0.1:8432/postgres";
    private static String driver = "org.postgresql.Driver";
    private static String user = "postgres";
    private static String password = "postgres";


    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            connection.setAutoCommit(true);

            String tableQuerySql = "SELECT tablename,schemaname FROM pg_catalog.pg_tables where schemaname not in ('pg_catalog', 'information_schema', 'sys')";
            //String allTable = "SELECT tablename,schemaname FROM pg_catalog.pg_tables";

            resultSet = statement.executeQuery(tableQuerySql);
            while (resultSet.next()) {

                log.info("Schema:{},Table:{}", resultSet.getString(2), resultSet.getString(1));
            }

            //do commit if autoCommit is false
        } catch (Exception e) {
            //do rollback if autoCommit is false

        } finally {
            JdbcUtils.close(resultSet);
            JdbcUtils.close(statement);
            JdbcUtils.close(connection);
        }


    }


}
