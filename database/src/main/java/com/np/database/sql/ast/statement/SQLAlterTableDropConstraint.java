
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLAlterTableDropConstraint extends SQLObjectImpl implements SQLAlterTableItem {

    private SQLName constraintName;

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.constraintName);
        }
        visitor.endVisit(this);
    }

    public SQLName getConstraintName() {
        return constraintName;
    }

    public void setConstraintName(SQLName constraintName) {
        this.constraintName = constraintName;
    }

}
