
package com.np.database.sql.ast.statement;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLReturnStatement extends SQLStatementImpl {

    private SQLExpr expr;

    public SQLReturnStatement() {

    }

    public SQLReturnStatement(String dbType) {
        super (dbType);
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

    public SQLReturnStatement clone() {
        SQLReturnStatement x = new SQLReturnStatement();
        if (expr != null) {
            x.setExpr(expr.clone());
        }
        return x;
    }

    @Override
    public List<SQLObject> getChildren() {
        return Collections.<SQLObject>singletonList(this.expr);
    }
}
