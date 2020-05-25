
package com.np.database.sql.ast.expr;

import com.np.database.sql.ast.SQLExpr;

public class SQLExprUtils {

    public static boolean equals(SQLExpr a, SQLExpr b) {
        if (a == b) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        Class<?> clazz_a = a.getClass();
        Class<?> clazz_b = b.getClass();
        if (clazz_a != clazz_b) {
            return false;
        }

        if (clazz_a == SQLIdentifierExpr.class) {
            SQLIdentifierExpr x_a = (SQLIdentifierExpr) a;
            SQLIdentifierExpr x_b = (SQLIdentifierExpr) b;
            return x_a.hashCode() == x_b.hashCode();
        }

        if (clazz_a == SQLBinaryOpExpr.class) {
            SQLBinaryOpExpr x_a = (SQLBinaryOpExpr) a;
            SQLBinaryOpExpr x_b = (SQLBinaryOpExpr) b;

            return x_a.equals(x_b);
        }

        return a.equals(b);
    }

    public static boolean isLiteralExpr(SQLExpr expr) {
        if (expr instanceof SQLLiteralExpr) {
            return true;
        }

        if (expr instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr binary = (SQLBinaryOpExpr) expr;
            return isLiteralExpr(binary.left) && isLiteralExpr(binary.right);
        }

        return false;
    }
}
