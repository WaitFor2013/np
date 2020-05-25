
package com.np.database.sql.ast;

import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLSubPartitionByList extends SQLSubPartitionBy {

    protected SQLName column;

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, column);
            acceptChild(visitor, subPartitionsCount);
        }
        visitor.endVisit(this);
    }

    public SQLName getColumn() {
        return column;
    }

    public void setColumn(SQLName column) {
        if (column != null) {
            column.setParent(this);
        }
        this.column = column;
    }

    public SQLSubPartitionByList clone() {
        SQLSubPartitionByList x = new SQLSubPartitionByList();
        if (column != null) {
            x.setColumn(column.clone());
        }
        return x;
    }

    public boolean isPartitionByColumn(long columnNameHashCode64) {
        return column != null
                && column.nameHashCode64() == columnNameHashCode64;
    }
}
