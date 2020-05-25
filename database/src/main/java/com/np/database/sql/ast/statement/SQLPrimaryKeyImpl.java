
package com.np.database.sql.ast.statement;

import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLPrimaryKeyImpl extends SQLUnique implements SQLPrimaryKey {
    protected boolean clustered         = false; // sql server

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.getName());
            acceptChild(visitor, this.getColumns());
        }
        visitor.endVisit(this);
    }

    public SQLPrimaryKeyImpl clone() {
        SQLPrimaryKeyImpl x = new SQLPrimaryKeyImpl();
        cloneTo(x);
        return x;
    }

    public void cloneTo(SQLPrimaryKeyImpl x) {
        super.cloneTo(x);
        x.clustered = clustered;
    }

    public boolean isClustered() {
        return clustered;
    }

    public void setClustered(boolean clustered) {
        this.clustered = clustered;
    }
}
