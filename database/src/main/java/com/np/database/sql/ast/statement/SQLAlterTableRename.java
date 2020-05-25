
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.ast.expr.SQLIdentifierExpr;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLAlterTableRename extends SQLObjectImpl implements SQLAlterTableItem {

    protected SQLExprTableSource to;

    public SQLAlterTableRename() {

    }

    public SQLAlterTableRename(SQLExpr to) {
        this.setTo(to);
    }

    public SQLAlterTableRename(String to) {
        this.setTo(to);
    }

    public SQLExprTableSource getTo() {
        return to;
    }

    public SQLName getToName() {
        if (to == null) {
            return null;
        }

        SQLExpr expr = to.expr;

        if (expr instanceof SQLName) {
            return (SQLName) expr;
        }

        return null;
    }

    public void setTo(SQLExprTableSource to) {
        if (to != null) {
            to.setParent(this);
        }
        this.to = to;
    }

    public void setTo(String to) {
        this.setTo(new SQLIdentifierExpr(to));
    }

    public void setTo(SQLExpr to) {
        this.setTo(new SQLExprTableSource(to));
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, to);
        }
        visitor.endVisit(this);
    }

}
