
package com.np.database.sql.builder;

import com.np.database.sql.builder.impl.SQLDeleteBuilderImpl;
import com.np.database.sql.builder.impl.SQLSelectBuilderImpl;
import com.np.database.sql.builder.impl.SQLUpdateBuilderImpl;

public class SQLBuilderFactory {

    public static SQLSelectBuilder createSelectSQLBuilder(String dbType) {
        return new SQLSelectBuilderImpl(dbType);
    }
    
    public static SQLSelectBuilder createSelectSQLBuilder(String sql, String dbType) {
        return new SQLSelectBuilderImpl(sql, dbType);
    }

    public static SQLDeleteBuilder createDeleteBuilder(String dbType) {
        return new SQLDeleteBuilderImpl(dbType);
    }
    
    public static SQLDeleteBuilder createDeleteBuilder(String sql, String dbType) {
        return new SQLDeleteBuilderImpl(sql, dbType);
    }

    public static SQLUpdateBuilder createUpdateBuilder(String dbType) {
        return new SQLUpdateBuilderImpl(dbType);
    }
    
    public static SQLUpdateBuilder createUpdateBuilder(String sql, String dbType) {
        return new SQLUpdateBuilderImpl(sql, dbType);
    }
}
