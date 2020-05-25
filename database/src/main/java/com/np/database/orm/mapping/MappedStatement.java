package com.np.database.orm.mapping;


import com.np.database.orm.SqlCommandType;
import com.np.database.orm.executor.parameter.LanguageDriver;
import com.np.database.orm.executor.parameter.LanguageDriver;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * @author Clinton Begin
 * <p>
 * <p>
 * modified by ch
 */
@Getter
@Setter
public final class MappedStatement {

    private SqlSource sqlSource;

    private ParameterMap parameterMap;
    private ResultMap resultMap;

    // 语句类型，INSERT、UPDATE...
    private SqlCommandType sqlCommandType;

    //动态SQL处理，mybatis基于XML配置
    private LanguageDriver lang;

    MappedStatement() {

    }

    public static class Builder {
        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(SqlSource sqlSource, SqlCommandType sqlCommandType) {
            mappedStatement.sqlSource = sqlSource;

            mappedStatement.parameterMap = new ParameterMap.Builder("defaultParameterMap", null, new ArrayList<>()).build();
            mappedStatement.sqlCommandType = sqlCommandType;

        }

        public Builder parameterMap(ParameterMap parameterMap) {
            mappedStatement.parameterMap = parameterMap;
            return this;
        }

        public Builder lang(LanguageDriver driver) {
            mappedStatement.lang = driver;
            return this;
        }

        public Builder resultMap(ResultMap resultMap) {
            mappedStatement.resultMap = resultMap;
            return this;
        }


        public MappedStatement build() {
            assert mappedStatement.sqlSource != null;
            assert mappedStatement.lang != null;
            assert mappedStatement.resultMap != null;

            return mappedStatement;
        }

    }

    public BoundSql getBoundSql(Object parameterObject) {

        return sqlSource.getBoundSql(parameterObject);
    }

}
