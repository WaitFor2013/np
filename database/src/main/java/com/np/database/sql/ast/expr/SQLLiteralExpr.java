
package com.np.database.sql.ast.expr;

import com.np.database.sql.ast.SQLExpr;

public interface SQLLiteralExpr extends SQLExpr {
    SQLLiteralExpr clone();
}
