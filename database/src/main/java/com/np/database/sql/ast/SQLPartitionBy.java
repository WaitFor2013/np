
package com.np.database.sql.ast;

import com.np.database.sql.ast.expr.SQLIdentifierExpr;
import com.np.database.sql.ast.expr.SQLIdentifierExpr;

import java.util.ArrayList;
import java.util.List;

public abstract class SQLPartitionBy extends SQLObjectImpl {
    protected SQLSubPartitionBy  subPartitionBy;
    protected SQLExpr            partitionsCount;
    protected boolean            linear;
    protected List<SQLPartition> partitions = new ArrayList<SQLPartition>();
    protected List<SQLName>      storeIn    = new ArrayList<SQLName>();
    protected List<SQLExpr>      columns    = new ArrayList<SQLExpr>();

    public List<SQLPartition> getPartitions() {
        return partitions;
    }
    
    public void addPartition(SQLPartition partition) {
        if (partition != null) {
            partition.setParent(this);
        }
        this.partitions.add(partition);
    }

    public SQLSubPartitionBy getSubPartitionBy() {
        return subPartitionBy;
    }

    public void setSubPartitionBy(SQLSubPartitionBy subPartitionBy) {
        if (subPartitionBy != null) {
            subPartitionBy.setParent(this);
        }
        this.subPartitionBy = subPartitionBy;
    }

    public SQLExpr getPartitionsCount() {
        return partitionsCount;
    }

    public void setPartitionsCount(SQLExpr partitionsCount) {
        if (partitionsCount != null) {
            partitionsCount.setParent(this);
        }
        this.partitionsCount = partitionsCount;
    }

    public boolean isLinear() {
        return linear;
    }

    public void setLinear(boolean linear) {
        this.linear = linear;
    }

    public List<SQLName> getStoreIn() {
        return storeIn;
    }

    public List<SQLExpr> getColumns() {
        return columns;
    }

    public void addColumn(SQLExpr column) {
        if (column != null) {
            column.setParent(this);
        }
        this.columns.add(column);
    }

    public void cloneTo(SQLPartitionBy x) {
        if (subPartitionBy != null) {
            x.setSubPartitionBy(subPartitionBy.clone());
        }
        if (partitionsCount != null) {
            x.setPartitionsCount(partitionsCount.clone());
        }
        x.linear = linear;
        for (SQLPartition p : partitions) {
            SQLPartition p2 = p.clone();
            p2.setParent(x);
            x.partitions.add(p2);
        }
        for (SQLName name : storeIn) {
            SQLName name2 = name.clone();
            name2.setParent(x);
            x.storeIn.add(name2);
        }
    }

    public boolean isPartitionByColumn(long columnNameHashCode64) {
        for (SQLExpr column : columns) {
            if (column instanceof SQLIdentifierExpr
                    && ((SQLIdentifierExpr) column)
                    .nameHashCode64() == columnNameHashCode64) {
                return true;
            }
        }

        if (subPartitionBy != null) {
            return subPartitionBy.isPartitionByColumn(columnNameHashCode64);
        }
        return false;
    }

    public abstract SQLPartitionBy clone();
}
