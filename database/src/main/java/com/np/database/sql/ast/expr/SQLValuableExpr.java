
package com.np.database.sql.ast.expr;

import com.np.database.sql.ast.SQLExpr;

public interface SQLValuableExpr extends SQLExpr {

    Object getValue();

}
