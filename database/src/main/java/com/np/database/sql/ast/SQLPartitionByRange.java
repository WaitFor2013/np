
package com.np.database.sql.ast;

import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLPartitionByRange extends SQLPartitionBy {
    protected SQLExpr       interval;

    public SQLPartitionByRange() {

    }

    public SQLExpr getInterval() {
        return interval;
    }

    public void setInterval(SQLExpr interval) {
        if (interval != null) {
            interval.setParent(this);
        }
        
        this.interval = interval;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, columns);
            acceptChild(visitor, interval);
            acceptChild(visitor, storeIn);
            acceptChild(visitor, partitions);
        }
        visitor.endVisit(this);
    }

    public SQLPartitionByRange clone() {
        SQLPartitionByRange x = new SQLPartitionByRange();

        if (interval != null) {
            x.setInterval(interval.clone());
        }

        for (SQLExpr column : columns) {
            SQLExpr c2 = column.clone();
            c2.setParent(x);
            x.columns.add(c2);
        }

        return x;
    }
}
