package com.np.database.orm;


import com.np.database.exception.NpDbException;
import com.np.database.orm.connection.pool.PooledDataSource;
import com.np.database.orm.connection.pool.PooledDataSourceFactory;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Properties;


@Slf4j
public class ConnectionTest {

    public static void main(String[] args) {

        //When auto-commit is disabled, the user must call either the commit or rollback method explicitly to end a transaction
        //事务控制涉及到多个语句，所以auto-commit必然为false.
        //针对查询语句，auto-commit可以为true,因为有些数据库是从查询语句开始事务的。
        //针对增删查改，auto-commit需要为false,手动提交，！！！！！如果发生错误一定要手动回滚。！！！！！
        Properties properties = new Properties();
        properties.setProperty("driver", "org.postgresql.Driver");
        properties.setProperty("url", "jdbc:postgresql://10.51.14.12:8432/postgres?loginTimeout=5");
        properties.setProperty("username", "postgres");
        properties.setProperty("password", "postgres");
        properties.setProperty("poolPingQuery", "select 1");
        properties.setProperty("poolPingEnabled", "true");
        properties.setProperty("poolPingConnectionsNotUsedFor", "5000");

        /**
         * // OPTIONAL CONFIGURATION FIELDS
         *   protected int poolMaximumActiveConnections = 10;   连接池最大连接数10
         *   protected int poolMaximumIdleConnections = 5;      连接池最大空闲连接数5
         *   protected int poolMaximumCheckoutTime = 20000;     连接最大超时时间20秒
         *   protected int poolTimeToWait = 20000;              最大等待时间20秒
         *
         *   发生断线处理逻辑，核心要在connect上面增加重试
         *   protected int poolMaximumLocalBadConnectionTolerance = 3;      如果失败的连接超过3个则停止尝试
         *   protected String poolPingQuery = "NO PING QUERY SET";          连接PING测试
         *   protected boolean poolPingEnabled;                             连接拼开启，重复使用链接时是否启动连接检查
         *   protected int poolPingConnectionsNotUsedFor;                   多长时间后未使用，才开启ping检查
         */

        //PostgreSQL官方连接参数,https://jdbc.postgresql.org/documentation/head/connect.html
        PooledDataSourceFactory dataSourceFactory = new PooledDataSourceFactory();
        dataSourceFactory.setProperties(properties);
        PooledDataSource dataSource = (PooledDataSource) dataSourceFactory.getDataSource();


        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);

            //while (true) {
            /*sys_kksss
             * do what you want start.
             */

            String allTable = "SELECT * FROM pg_catalog.pg_tables where schemaname not in ('pg_catalog','information_schema', 'sys')";
            //String allTable = "SELECT * FROM information_schema.columns  where table_name='schema_table'    ";
            statement = con.prepareStatement(allTable);

            resultSet = statement.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();
            //System.out.println(metaData.getColumnCount());
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                //System.out.println(i);
                System.out.print(metaData.getColumnLabel(i) + " ");
            }
            System.out.println();

            while (resultSet.next()) {
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    Object object = resultSet.getObject(i);
                    System.out.print(object == null ? "null   " : resultSet.getObject(i) + "   ");
                }
                System.out.println();
                //log.info("Schema:{},Table:{}", resultSet.getString(2), resultSet.getString(1));
            }

            /*
             * do what you want end.
             */
            con.commit();

            //System.out.println(dataSource.getPoolState().toString());
            //}
        } catch (Throwable e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException se) {
                    log.error("NP connection rollback failed.", se);
                }
            }
            throw new NpDbException(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException se) {
                    log.error("NP resultSet close failed.", se);
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException se) {
                    log.error("NP statement rollback failed.", se);
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (SQLException se) {
                    log.error("NP connection rollback failed.", se);
                }
            }
        }

    }
}
