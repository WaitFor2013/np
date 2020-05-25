package com.np.database.orm.session;


import com.np.database.ColumnDefinition;
import com.np.database.NoRepeatView;
import com.np.database.orm.biz.param.BizParam;

import java.sql.Connection;
import java.util.List;

public interface SqlSession extends AutoCloseable {

    Connection getConnection();

    <T> T queryById(T parameter);

    <E> List<E> queryAll(BizParam parameter, Class<E> resultType);

    int count(BizParam parameter, Class resultType);

    <E extends NoRepeatView> List<E> queryView(BizParam param, Class<E> resultType);

    <E extends NoRepeatView> int countView(BizParam param, Class<E> resultType);

    //<E> List<E> query(String sql, BizParam parameter, Class<E> resultType);

    //int count(String sql, BizParam parameter, Class resultType);

    int create(Object parameter);

    int updateById(Object parameter);

    int deleteById(Object parameter);

    int updateByColumns(Object parameter, ColumnDefinition... columns);

    int deleteByColumns(Object parameter, ColumnDefinition... columns);

    void commit();

    void rollback();

}
