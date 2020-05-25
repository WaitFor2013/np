
package com.np.database.sql.dialect.oracle.ast.clause;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public class PartitionExtensionClause extends OracleSQLObjectImpl {

    private boolean             subPartition;
    private SQLName partition;
    private final List<SQLName> target = new ArrayList<SQLName>();

    public boolean isSubPartition() {
        return subPartition;
    }

    public void setSubPartition(boolean subPartition) {
        this.subPartition = subPartition;
    }

    public SQLName getPartition() {
        return partition;
    }

    public void setPartition(SQLName partition) {
        this.partition = partition;
    }

    public List<SQLName> getFor() {
        return target;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, partition);
            acceptChild(visitor, target);
        }
        visitor.endVisit(this);
    }

    public PartitionExtensionClause clone() {
        PartitionExtensionClause x = new PartitionExtensionClause();

        x.subPartition = subPartition;
        if (partition != null) {
            x.setPartition(partition.clone());
        }

        for (SQLName item : target) {
            SQLName item1 = item.clone();
            item1.setParent(x);
            x.target.add(item1);
        }

        return x;
    }
}
