
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLColumnCheck extends SQLConstraintImpl implements SQLColumnConstraint {

    private SQLExpr expr;

    public SQLColumnCheck(){

    }

    public SQLColumnCheck(SQLExpr expr){
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
            acceptChild(visitor, this.getName());
            acceptChild(visitor, this.getExpr());
        }
        visitor.endVisit(this);
    }

    public SQLColumnCheck clone() {
        SQLColumnCheck x = new SQLColumnCheck();

        super.cloneTo(x);

        if (expr != null) {
            x.setExpr(expr.clone());
        }

        return x;
    }

}
