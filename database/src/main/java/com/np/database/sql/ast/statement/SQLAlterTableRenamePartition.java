
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLAlterTableRenamePartition extends SQLObjectImpl implements SQLAlterTableItem {

    private boolean ifNotExists = false;

    private final List<SQLAssignItem> partition = new ArrayList<SQLAssignItem>(4);
    private final List<SQLAssignItem> to        = new ArrayList<SQLAssignItem>(4);

    public List<SQLAssignItem> getPartition() {
        return partition;
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public void setIfNotExists(boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }

    public List<SQLAssignItem> getTo() {
        return to;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, partition);
            acceptChild(visitor, to);
        }
        visitor.endVisit(this);
    }
}
