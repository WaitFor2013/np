
package com.np.database.sql.ast;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.statement.SQLSelectOrderByItem;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.statement.SQLSelectOrderByItem;

public final class SQLOrderBy extends SQLObjectImpl {

    protected final List<SQLSelectOrderByItem> items = new ArrayList<SQLSelectOrderByItem>();
    
    // for postgres
    private boolean                            sibings;

    public SQLOrderBy(){

    }

    public SQLOrderBy(SQLExpr expr){
        SQLSelectOrderByItem item = new SQLSelectOrderByItem(expr);
        addItem(item);
    }

    public SQLOrderBy(SQLExpr expr, SQLOrderingSpecification type){
        SQLSelectOrderByItem item = new SQLSelectOrderByItem(expr, type);
        addItem(item);
    }

    public void addItem(SQLSelectOrderByItem item) {
        if (item != null) {
            item.setParent(this);
        }
        this.items.add(item);
    }

    public void addItem(SQLExpr item) {
        addItem(new SQLSelectOrderByItem(item));
    }

    public List<SQLSelectOrderByItem> getItems() {
        return this.items;
    }
    
    public boolean isSibings() {
        return this.sibings;
    }

    public void setSibings(boolean sibings) {
        this.sibings = sibings;
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.items);
        }

        visitor.endVisit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((items == null) ? 0 : items.hashCode());
        result = prime * result + (sibings ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SQLOrderBy other = (SQLOrderBy) obj;
        if (items == null) {
            if (other.items != null) return false;
        } else if (!items.equals(other.items)) return false;
        if (sibings != other.sibings) return false;
        return true;
    }

    public void addItem(SQLExpr expr, SQLOrderingSpecification type) {
        SQLSelectOrderByItem item = createItem();
        item.setExpr(expr);
        item.setType(type);
        addItem(item);
    }

    protected SQLSelectOrderByItem createItem() {
        return new SQLSelectOrderByItem();
    }

    public SQLOrderBy clone() {
        SQLOrderBy x = new SQLOrderBy();

        for (SQLSelectOrderByItem item : items) {
            SQLSelectOrderByItem item1 = item.clone();
            item1.setParent(x);
            x.items.add(item1);
        }

        x.sibings = sibings;

        return x;
    }
}
