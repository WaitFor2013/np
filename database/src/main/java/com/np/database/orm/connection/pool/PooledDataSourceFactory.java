package com.np.database.orm.connection.pool;


import com.np.database.orm.connection.pool.base.UnpooledDataSourceFactory;
import com.np.database.orm.connection.pool.base.UnpooledDataSourceFactory;

/**
 * @author Clinton Begin
 */
public class PooledDataSourceFactory extends UnpooledDataSourceFactory {

    public PooledDataSourceFactory() {
        this.dataSource = new PooledDataSource();
    }

}
