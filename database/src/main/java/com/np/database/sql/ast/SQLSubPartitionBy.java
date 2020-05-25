
package com.np.database.sql.ast;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.statement.SQLAssignItem;
import com.np.database.sql.ast.statement.SQLAssignItem;

public abstract class SQLSubPartitionBy extends SQLObjectImpl {

    protected SQLExpr               subPartitionsCount;
    protected boolean               linear;
    protected List<SQLAssignItem>   options              = new ArrayList<SQLAssignItem>();
    protected List<SQLSubPartition> subPartitionTemplate = new ArrayList<SQLSubPartition>();

    public SQLExpr getSubPartitionsCount() {
        return subPartitionsCount;
    }

    public void setSubPartitionsCount(SQLExpr subPartitionsCount) {
        if (subPartitionsCount != null) {
            subPartitionsCount.setParent(this);
        }

        this.subPartitionsCount = subPartitionsCount;
    }

    public boolean isLinear() {
        return linear;
    }

    public void setLinear(boolean linear) {
        this.linear = linear;
    }

    public List<SQLAssignItem> getOptions() {
        return options;
    }

    public List<SQLSubPartition> getSubPartitionTemplate() {
        return subPartitionTemplate;
    }

    public void cloneTo(SQLSubPartitionBy x) {
        if (subPartitionsCount != null) {
            x.setSubPartitionsCount(subPartitionsCount.clone());
        }
        x.linear = linear;
        for (SQLAssignItem option : options) {
            SQLAssignItem option2 = option.clone();
            option2.setParent(x);
            x.options.add(option2);
        }

        for (SQLSubPartition p : subPartitionTemplate) {
            SQLSubPartition p2 = p.clone();
            p2.setParent(x);
            x.subPartitionTemplate.add(p2);
        }
    }

    public boolean isPartitionByColumn(long columnNameHashCode64) {
        return false;
    }

    public abstract SQLSubPartitionBy clone();
}
