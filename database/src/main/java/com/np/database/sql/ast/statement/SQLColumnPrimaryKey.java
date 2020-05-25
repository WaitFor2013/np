
package com.np.database.sql.ast.statement;

import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLColumnPrimaryKey extends SQLConstraintImpl implements SQLColumnConstraint {
    public SQLColumnPrimaryKey() {

    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.getName());
        }
        visitor.endVisit(this);
    }

    public SQLColumnPrimaryKey clone() {
        SQLColumnPrimaryKey x = new SQLColumnPrimaryKey();

        super.cloneTo(x);

        return x;
    }
}
