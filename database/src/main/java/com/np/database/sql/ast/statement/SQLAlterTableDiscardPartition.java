
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLAlterTableDiscardPartition extends SQLObjectImpl implements SQLAlterTableItem {

    private final List<SQLName> partitions = new ArrayList<SQLName>(4);
    private boolean tablespace;

    public List<SQLName> getPartitions() {
        return partitions;
    }
    
    public void addPartition(SQLName partition) {
        if (partition != null) {
            partition.setParent(this);
        }
        this.partitions.add(partition);
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, partitions);
        }
        visitor.endVisit(this);
    }

    public boolean isTablespace() {
        return tablespace;
    }

    public void setTablespace(boolean tablespace) {
        this.tablespace = tablespace;
    }
}
