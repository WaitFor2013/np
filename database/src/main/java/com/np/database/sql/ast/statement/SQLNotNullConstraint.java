
package com.np.database.sql.ast.statement;

import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLNotNullConstraint extends SQLConstraintImpl implements SQLColumnConstraint {

    public SQLNotNullConstraint(){

    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    public SQLNotNullConstraint clone() {
        SQLNotNullConstraint x = new SQLNotNullConstraint();
        super.cloneTo(x);
        return x;
    }
}
