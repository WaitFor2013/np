
package com.np.database.sql.ast.expr;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLExprImpl;

public abstract class SQLNumericLiteralExpr extends SQLExprImpl implements SQLLiteralExpr {

    public SQLNumericLiteralExpr(){

    }

    public abstract Number getNumber();

    public abstract void setNumber(Number number);

    public abstract SQLNumericLiteralExpr clone();

    @Override
    public List getChildren() {
        return Collections.emptyList();
    }
}
