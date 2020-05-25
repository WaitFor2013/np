
package com.np.database.sql.ast;

import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLPartitionByList extends SQLPartitionBy {
    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, columns);
            acceptChild(visitor, partitionsCount);
            acceptChild(visitor, getPartitions());
            acceptChild(visitor, subPartitionBy);
        }
        visitor.endVisit(this);
    }

    public SQLPartitionByList clone() {
        SQLPartitionByList x = new SQLPartitionByList();

        for (SQLExpr column : columns) {
            SQLExpr c2 = column.clone();
            c2.setParent(x);
            x.columns.add(c2);
        }

        return x;
    }
}
