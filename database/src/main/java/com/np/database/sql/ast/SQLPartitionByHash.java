
package com.np.database.sql.ast;

import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLPartitionByHash extends SQLPartitionBy {

    // for aliyun ads
    protected boolean key;
    protected boolean unique;

    public boolean isKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, partitionsCount);
            acceptChild(visitor, getPartitions());
            acceptChild(visitor, subPartitionBy);
        }
        visitor.endVisit(this);
    }

    public SQLPartitionByHash clone() {
        SQLPartitionByHash x = new SQLPartitionByHash();

        cloneTo(x);

        x.key = key;
        x.unique = unique;

        for (SQLExpr column : columns) {
            SQLExpr c2 = column.clone();
            c2.setParent(x);
            x.columns.add(c2);
        }

        return x;
    }

    public void cloneTo(SQLPartitionByHash x) {
        super.cloneTo(x);
    }
}
