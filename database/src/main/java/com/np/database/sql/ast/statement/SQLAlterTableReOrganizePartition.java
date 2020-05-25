
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLAlterTableReOrganizePartition extends SQLObjectImpl implements SQLAlterTableItem {

    private final List<SQLName>   names       = new ArrayList<SQLName>();

    private final List<SQLObject> partitions  = new ArrayList<SQLObject>(4);

    public List<SQLObject> getPartitions() {
        return partitions;
    }

    public void addPartition(SQLObject partition) {
        if (partition != null) {
            partition.setParent(this);
        }
        this.partitions.add(partition);
    }

    public List<SQLName> getNames() {
        return names;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, partitions);
        }
        visitor.endVisit(this);
    }
}
