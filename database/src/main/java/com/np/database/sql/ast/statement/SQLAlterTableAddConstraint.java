
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLAlterTableAddConstraint extends SQLObjectImpl implements SQLAlterTableItem {

    private SQLConstraint constraint;
    private boolean      withNoCheck = false;

    public SQLAlterTableAddConstraint(){

    }

    public SQLAlterTableAddConstraint(SQLConstraint constraint){
        this.setConstraint(constraint);
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, constraint);
        }
        visitor.endVisit(this);
    }

    public SQLConstraint getConstraint() {
        return constraint;
    }

    public void setConstraint(SQLConstraint constraint) {
        if (constraint != null) {
            constraint.setParent(this);
        }
        this.constraint = constraint;
    }

    public boolean isWithNoCheck() {
        return withNoCheck;
    }

    public void setWithNoCheck(boolean withNoCheck) {
        this.withNoCheck = withNoCheck;
    }

}
