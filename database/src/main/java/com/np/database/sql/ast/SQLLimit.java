
package com.np.database.sql.ast;

import com.np.database.sql.ast.expr.SQLIntegerExpr;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.expr.SQLIntegerExpr;

/**
 * Created by wenshao on 16/9/25.
 */
public final class SQLLimit extends SQLObjectImpl {

    public SQLLimit() {

    }

    public SQLLimit(SQLExpr rowCount) {
        this.setRowCount(rowCount);
    }

    public SQLLimit(SQLExpr offset, SQLExpr rowCount) {
        this.setOffset(offset);
        this.setRowCount(rowCount);
    }

    private SQLExpr rowCount;
    private SQLExpr offset;

    public SQLExpr getRowCount() {
        return rowCount;
    }

    public void setRowCount(SQLExpr rowCount) {
        if (rowCount != null) {
            rowCount.setParent(this);
        }
        this.rowCount = rowCount;
    }

    public void setRowCount(int rowCount) {
        this.setRowCount(new SQLIntegerExpr(rowCount));
    }

    public SQLExpr getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.setOffset(new SQLIntegerExpr(offset));
    }

    public void setOffset(SQLExpr offset) {
        if (offset != null) {
            offset.setParent(this);
        }
        this.offset = offset;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, offset);
            acceptChild(visitor, rowCount);
        }
        visitor.endVisit(this);
    }

    public SQLLimit clone() {
        SQLLimit x = new SQLLimit();

        if (offset != null) {
            x.setOffset(offset.clone());
        }

        if (rowCount != null) {
            x.setRowCount(rowCount.clone());
        }

        return x;
    }

}
