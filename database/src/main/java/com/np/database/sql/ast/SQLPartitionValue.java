
package com.np.database.sql.ast;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLPartitionValue extends SQLObjectImpl {

    protected Operator            operator;
    protected final List<SQLExpr> items = new ArrayList<SQLExpr>();

    public SQLPartitionValue(Operator operator){
        super();
        this.operator = operator;
    }

    public List<SQLExpr> getItems() {
        return items;
    }
    
    public void addItem(SQLExpr item) {
        if (item != null) {
            item.setParent(this);
        }
        this.items.add(item);
    }

    public Operator getOperator() {
        return operator;
    }

    public static enum Operator {
                                 LessThan, //
                                 In, //
                                 List
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, getItems());
        }
        visitor.endVisit(this);
    }

    public SQLPartitionValue clone() {
        SQLPartitionValue x = new SQLPartitionValue(operator);

        for (SQLExpr item : items) {
            SQLExpr item2 = item.clone();
            item2.setParent(x);
            x.items.add(item2);
        }

        return x;
    }
}
