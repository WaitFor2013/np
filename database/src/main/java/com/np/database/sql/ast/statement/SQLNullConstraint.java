
package com.np.database.sql.ast.statement;

import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLNullConstraint extends SQLConstraintImpl implements SQLColumnConstraint {

    public SQLNullConstraint(){
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    public SQLNullConstraint clone() {
        SQLNullConstraint x = new SQLNullConstraint();
        super.cloneTo(x);
        return x;
    }
}
