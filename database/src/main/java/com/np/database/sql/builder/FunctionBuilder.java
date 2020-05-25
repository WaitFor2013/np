
package com.np.database.sql.builder;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;
import com.np.database.sql.util.JdbcConstants;

/**
 * Created by wenshao on 09/07/2017.
 */
public class FunctionBuilder {
    private final String dbType;

    public FunctionBuilder(String dbType) {
        this.dbType = dbType;
    }

    // for character function
    public SQLMethodInvokeExpr length(SQLExpr expr) {
        return new SQLMethodInvokeExpr("length", null, expr);
    }

    public SQLMethodInvokeExpr lower(SQLExpr expr) {
        return new SQLMethodInvokeExpr("lower", null, expr);
    }

    public SQLMethodInvokeExpr upper(SQLExpr expr) {
        return new SQLMethodInvokeExpr("upper", null, expr);
    }

    public SQLMethodInvokeExpr substr(SQLExpr expr) {
        return new SQLMethodInvokeExpr("substr", null, expr);
    }

    public SQLMethodInvokeExpr ltrim(SQLExpr expr) {
        return new SQLMethodInvokeExpr("ltrim", null, expr);
    }

    public SQLMethodInvokeExpr rtrim(SQLExpr expr) {
        return new SQLMethodInvokeExpr("rtrim", null, expr);
    }

    public SQLMethodInvokeExpr trim(SQLExpr expr) {
        return new SQLMethodInvokeExpr("trim", null, expr);
    }

    public SQLMethodInvokeExpr ifnull(SQLExpr expr1, SQLExpr expr2) {
        if (JdbcConstants.ALIYUN_ADS.equals(dbType)
                || JdbcConstants.PRESTO.equals(dbType)
                || JdbcConstants.ODPS.equals(dbType)) {
            return new SQLMethodInvokeExpr("coalesce", null, expr1, expr2);
        }

        if (JdbcConstants.ORACLE.equals(dbType)) {
            return new SQLMethodInvokeExpr("nvl", null, expr1, expr2);
        }

        if (JdbcConstants.SQL_SERVER.equals(dbType)) {
            return new SQLMethodInvokeExpr("isnull", null, expr1, expr2);
        }

        return new SQLMethodInvokeExpr("ifnull", null, expr1, expr2);
    }
}
