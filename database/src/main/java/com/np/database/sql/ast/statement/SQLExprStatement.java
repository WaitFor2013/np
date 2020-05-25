
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLExprStatement extends SQLStatementImpl {

    private SQLExpr expr;

    public SQLExprStatement(){

    }

    public SQLExprStatement(SQLExpr expr){
        this.expr = expr;
    }

    @Override
    public void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, expr);
        }
        visitor.endVisit(this);

    }

    public SQLExpr getExpr() {
        return expr;
    }

    public void setExpr(SQLExpr expr) {
        this.expr = expr;
    }

}
