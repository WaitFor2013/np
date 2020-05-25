
package com.np.database.sql.builder.impl;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLBooleanExpr;
import com.np.database.sql.ast.expr.SQLCharExpr;
import com.np.database.sql.ast.expr.SQLIntegerExpr;
import com.np.database.sql.ast.expr.SQLNullExpr;
import com.np.database.sql.ast.expr.SQLNumberExpr;
import com.np.database.sql.builder.SQLBuilder;


public class SQLBuilderImpl implements SQLBuilder {
    public static SQLExpr toSQLExpr(Object obj, String dbType) {
        if (obj == null) {
            return new SQLNullExpr();
        }
        
        if (obj instanceof Integer) {
            return new SQLIntegerExpr((Integer) obj);
        }
        
        if (obj instanceof Number) {
            return new SQLNumberExpr((Number) obj);
        }
        
        if (obj instanceof String) {
            return new SQLCharExpr((String) obj);
        }
        
        if (obj instanceof Boolean) {
            return new SQLBooleanExpr((Boolean) obj);
        }
        
        throw new IllegalArgumentException("not support : " + obj.getClass().getName());
    }
}
