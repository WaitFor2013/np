
package com.np.database.sql.ast;

import com.np.database.sql.visitor.SQLASTVisitor;

public final class SQLKeep extends SQLObjectImpl {

    protected DenseRank  denseRank;

    protected SQLOrderBy orderBy;

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.orderBy);
        }
        visitor.endVisit(this);
    }

    public DenseRank getDenseRank() {
        return denseRank;
    }

    public void setDenseRank(DenseRank denseRank) {
        this.denseRank = denseRank;
    }

    public SQLOrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(SQLOrderBy orderBy) {
        if (orderBy != null) {
            orderBy.setParent(this);
        }
        this.orderBy = orderBy;
    }


    public SQLKeep clone() {
        SQLKeep x = new SQLKeep();

        x.denseRank = denseRank;

        if (orderBy != null) {
            x.setOrderBy(orderBy.clone());
        }

        return x;
    }

    public static enum DenseRank {
                                  FIRST, //
                                  LAST
    }
}
