package com.np.database.orm.connection.pool.base;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author Clinton Begin
 */
public interface DataSourceFactory {

  void setProperties(Properties props);

  DataSource getDataSource();

}
