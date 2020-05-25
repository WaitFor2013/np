
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLAlterTableAddPartition extends SQLObjectImpl implements SQLAlterTableItem {

    private boolean               ifNotExists = false;

    private final List<SQLObject> partitions  = new ArrayList<SQLObject>(4);

    private SQLExpr               partitionCount;

    public List<SQLObject> getPartitions() {
        return partitions;
    }

    public void addPartition(SQLObject partition) {
        if (partition != null) {
            partition.setParent(this);
        }
        this.partitions.add(partition);
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public void setIfNotExists(boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }

    public SQLExpr getPartitionCount() {
        return partitionCount;
    }

    public void setPartitionCount(SQLExpr partitionCount) {
        if (partitionCount != null) {
            partitionCount.setParent(this);
        }
        this.partitionCount = partitionCount;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, partitions);
        }
        visitor.endVisit(this);
    }
}
