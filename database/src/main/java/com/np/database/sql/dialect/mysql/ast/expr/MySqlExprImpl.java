
package com.np.database.sql.dialect.mysql.ast.expr;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.mysql.ast.MySqlObjectImpl;

public abstract class MySqlExprImpl extends MySqlObjectImpl implements SQLExpr {
    public SQLExpr clone() {
        throw new UnsupportedOperationException();
    }
}
