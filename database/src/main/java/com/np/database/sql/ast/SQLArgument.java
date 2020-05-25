
package com.np.database.sql.ast;

import com.np.database.sql.visitor.SQLASTVisitor;

/**
 * Created by wenshao on 29/05/2017.
 */
public class SQLArgument extends SQLObjectImpl {
    private SQLParameter.ParameterType type;
    private SQLExpr expr;

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, expr);
        }

        visitor.endVisit(this);
    }

    public SQLArgument clone() {
        SQLArgument x = new SQLArgument();

        x.type = type;

        if (expr != null) {
            x.setExpr(expr.clone());
        }

        return x;
    }

    public SQLParameter.ParameterType getType() {
        return type;
    }

    public SQLExpr getExpr() {
        return expr;
    }

    public void setType(SQLParameter.ParameterType type) {
        this.type = type;
    }

    public void setExpr(SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.expr = x;
    }
}
