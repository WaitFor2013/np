
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.ast.expr.SQLBinaryOpExpr;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLSelectGroupByClause extends SQLObjectImpl {

    private final List<SQLExpr> items = new ArrayList<SQLExpr>();
    private SQLExpr             having;
    private boolean             withRollUp = false;
    private boolean             withCube = false;
    private boolean             distinct   = false;
    private boolean             paren      = false;

    public SQLSelectGroupByClause(){

    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.items);
            acceptChild(visitor, this.having);
        }

        visitor.endVisit(this);
    }
    
    public boolean isWithRollUp() {
        return withRollUp;
    }

    public void setWithRollUp(boolean withRollUp) {
        this.withRollUp = withRollUp;
    }
    
    
    public boolean isWithCube() {
        return withCube;
    }

    public void setWithCube(boolean withCube) {
        this.withCube = withCube;
    }

    public SQLExpr getHaving() {
        return this.having;
    }

    public void setHaving(SQLExpr having) {
        if (having != null) {
            having.setParent(this);
        }
        
        this.having = having;
    }

    public List<SQLExpr> getItems() {
        return this.items;
    }

    public void addItem(SQLExpr sqlExpr) {
        if (sqlExpr != null) {
            sqlExpr.setParent(this);
            this.items.add(sqlExpr);
        }
    }

    public SQLSelectGroupByClause clone() {
        SQLSelectGroupByClause x = new SQLSelectGroupByClause();
        for (SQLExpr item : items) {
            SQLExpr item2 = item.clone();
            item2.setParent(x);
            x.items.add(item2);
        }
        if (having != null) {
            x.setHaving(having.clone());
        }
        x.withRollUp = withRollUp;
        x.withCube = withCube;
        return x;
    }

    public void addHaving(SQLExpr condition) {
        if (condition == null) {
            return;
        }

        if (having == null) {
            having = condition;
        } else {
            having = SQLBinaryOpExpr.and(having, condition);
        }
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isParen() {
        return paren;
    }

    public void setParen(boolean paren) {
        this.paren = paren;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SQLSelectGroupByClause group = (SQLSelectGroupByClause) o;

        if (withRollUp != group.withRollUp) return false;
        if (withCube != group.withCube) return false;
        if (distinct != group.distinct) return false;
        if (paren != group.paren) return false;
        if (!items.equals(group.items)) return false;
        return having != null ? having.equals(group.having) : group.having == null;
    }

    @Override
    public int hashCode() {
        int result = items != null ? items.hashCode() : 0;
        result = 31 * result + (having != null ? having.hashCode() : 0);
        result = 31 * result + (withRollUp ? 1 : 0);
        result = 31 * result + (withCube ? 1 : 0);
        result = 31 * result + (distinct ? 1 : 0);
        result = 31 * result + (paren ? 1 : 0);
        return result;
    }
}
