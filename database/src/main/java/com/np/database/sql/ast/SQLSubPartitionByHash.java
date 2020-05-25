
package com.np.database.sql.ast;

import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLSubPartitionByHash extends SQLSubPartitionBy {

    protected SQLExpr expr;

    // for aliyun ads
    private boolean   key;

    public SQLExpr getExpr() {
        return expr;
    }

    public void setExpr(SQLExpr expr) {
        if (expr != null) {
            expr.setParent(this);
        }
        this.expr = expr;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, expr);
            acceptChild(visitor, subPartitionsCount);
        }
        visitor.endVisit(this);
    }

    public boolean isKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    public SQLSubPartitionByHash clone() {
        SQLSubPartitionByHash x = new SQLSubPartitionByHash();
        if (expr != null) {
            x.setExpr(expr.clone());
        }
        x.key = key;
        return x;
    }

    public boolean isPartitionByColumn(long columnNameHashCode64) {
        return expr instanceof SQLName
                && ((SQLName) expr).nameHashCode64() == columnNameHashCode64;
    }
}
