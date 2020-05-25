
package com.np.database.sql.ast.expr;

import static com.np.database.sql.visitor.SQLEvalVisitor.EVAL_VALUE_NULL;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public final class SQLNullExpr extends SQLExprImpl implements SQLLiteralExpr, SQLValuableExpr {

    public SQLNullExpr(){

    }

    public void output(StringBuffer buf) {
        buf.append("NULL");
    }

    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);

        visitor.endVisit(this);
    }

    public int hashCode() {
        return 0;
    }

    public boolean equals(Object o) {
        return o instanceof SQLNullExpr;
    }

    @Override
    public Object getValue() {
        return EVAL_VALUE_NULL;
    }

    public SQLNullExpr clone() {
        return new SQLNullExpr();
    }

    @Override
    public List getChildren() {
        return Collections.emptyList();
    }
}
