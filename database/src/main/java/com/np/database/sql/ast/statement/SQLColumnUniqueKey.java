
package com.np.database.sql.ast.statement;

import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLColumnUniqueKey extends SQLConstraintImpl implements SQLColumnConstraint {

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.getName());
        }
        visitor.endVisit(this);
    }

    public SQLColumnUniqueKey clone() {
        SQLColumnUniqueKey x = new SQLColumnUniqueKey();
        super.cloneTo(x);
        return x;
    }
}
