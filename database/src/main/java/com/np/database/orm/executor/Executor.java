package com.np.database.orm.executor;


import com.np.database.orm.mapping.MappedStatement;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Clinton Begin
 * <p>
 * <p>
 * modified by ch
 */
public interface Executor {

    int update(MappedStatement ms, Object parameter) throws SQLException;

    <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds) throws SQLException;

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void close(boolean forceRollback);

    boolean isClosed();

    void setExecutorWrapper(Executor executor);


    //游标功能当前用途不是很大，暂时不支持
    //<E> Cursor<E> queryCursor(MappedStatement ms, Object parameter, RowBounds rowBounds) throws SQLException;

    //SQL批量执行，暂时不支持
    //List<BatchResult> flushStatements() throws SQLException;

    //应用缓存基于Redis，不直接缓存数据在内存中
    //<E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey cacheKey, BoundSql boundSql) throws SQLException;

    //CacheKey createCacheKey(MappedStatement ms, Object parameterObject, RowBounds rowBounds, BoundSql boundSql);

    //boolean isCached(MappedStatement ms, CacheKey key);

    //void clearLocalCache();

    //void deferLoad(MappedStatement ms, MetaObject resultObject, String param, CacheKey key, Class<?> targetType);

    //事务直接基于连接处理，暂不支持扩展
    Connection getTransaction();

}
