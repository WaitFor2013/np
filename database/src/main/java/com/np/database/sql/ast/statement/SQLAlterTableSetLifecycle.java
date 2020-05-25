
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLAlterTableSetLifecycle extends SQLObjectImpl implements SQLAlterTableItem {

    private SQLExpr lifecycle;

    public SQLExpr getLifecycle() {
        return lifecycle;
    }

    public void setLifecycle(SQLExpr comment) {
        if (comment != null) {
            comment.setParent(this);
        }
        this.lifecycle = comment;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, lifecycle);
        }
        visitor.endVisit(this);
    }

}
