
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLAlterTableCoalescePartition extends SQLObjectImpl implements SQLAlterTableItem {

    private SQLExpr count;

    public SQLExpr getCount() {
        return count;
    }

    public void setCount(SQLExpr count) {
        if (count != null) {
            count.setParent(this);
        }
        this.count = count;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, count);
        }
        visitor.endVisit(this);
    }
}
