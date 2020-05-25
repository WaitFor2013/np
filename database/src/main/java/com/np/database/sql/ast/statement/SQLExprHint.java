
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLHint;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLExprHint extends SQLObjectImpl implements SQLHint {

    private SQLExpr expr;

    public SQLExprHint(){

    }

    public SQLExprHint(SQLExpr expr){
        this.setExpr(expr);
    }

    public SQLExpr getExpr() {
        return expr;
    }

    public void setExpr(SQLExpr expr) {
        if (expr != null) {
            expr.setParent(this);
        }

        this.expr = expr;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, expr);
        }
        visitor.endVisit(this);
    }

    public SQLExprHint clone() {
        SQLExprHint x = new SQLExprHint();
        if (expr != null) {
            x.setExpr(expr.clone());
        }
        return x;
    }
}
