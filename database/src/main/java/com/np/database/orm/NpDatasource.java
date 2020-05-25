package com.np.database.orm;


import com.np.database.exception.NpDbException;
import com.np.database.orm.biz.DbTypeEnum;
import com.np.database.orm.connection.pool.PooledDataSource;
import com.np.database.orm.connection.pool.PooledDataSourceFactory;
import com.np.database.orm.session.DefaultSqlSession;
import com.np.database.orm.session.SqlSession;
import com.np.database.orm.biz.DbTypeEnum;
import com.np.database.orm.connection.pool.PooledDataSource;
import com.np.database.orm.connection.pool.PooledDataSourceFactory;
import com.np.database.orm.session.DefaultSqlSession;
import com.np.database.orm.session.SqlSession;

import javax.sql.DataSource;
import java.util.Properties;

public class NpDatasource {

    private final PooledDataSource dataSource;
    private final DbTypeEnum dbTypeEnum;
    private final String name;

    public NpDatasource(NpDbConfig dbConfig) {

        if (null == dbConfig) {
            throw new NpDbException("dbConfig can't be null.");
        }
        dbConfig.validate();
        dbTypeEnum = dbConfig.getDbTypeEnum();

        name = dbConfig.getDbName() + "[" + dbConfig.getIp() + "]";
        Properties properties = new Properties();
        switch (dbConfig.getDbTypeEnum()) {
            case POSTGRESQL:

                if (null != dbConfig.getLog() && dbConfig.getLog()) {
                    properties.setProperty("driver", "net.sf.log4jdbc.DriverSpy");
                    properties.setProperty("url", String.format("jdbc:log4jdbc:postgresql://%s:%d/%s?loginTimeout=3&stringtype=unspecified", dbConfig.getIp(), dbConfig.getPort(), dbConfig.getDbName()));
                } else {
                    properties.setProperty("driver", "org.postgresql.Driver");
                    properties.setProperty("url", String.format("jdbc:postgresql://%s:%d/%s?loginTimeout=3&stringtype=unspecified", dbConfig.getIp(), dbConfig.getPort(), dbConfig.getDbName()));
                }
                properties.setProperty("username", dbConfig.getUser());
                properties.setProperty("password", dbConfig.getPassword());
                properties.setProperty("poolPingQuery", "select 1");
                properties.setProperty("poolPingEnabled", "true");
                properties.setProperty("poolPingConnectionsNotUsedFor", "5000");
                break;
            default:
                throw new NpDbException("NpDatasource not support " + dbConfig.getDbTypeEnum());
        }

        PooledDataSourceFactory dataSourceFactory = new PooledDataSourceFactory();
        dataSourceFactory.setProperties(properties);
        dataSource = (PooledDataSource) dataSourceFactory.getDataSource();

    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public String getName() {
        return name;
    }

    public SqlSession getSession(boolean autoCommit) {
        return new DefaultSqlSession(dataSource, dbTypeEnum, autoCommit);
    }
}
