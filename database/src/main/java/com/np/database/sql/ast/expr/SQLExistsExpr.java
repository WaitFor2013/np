
package com.np.database.sql.ast.expr;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.ast.statement.SQLSelect;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.statement.SQLSelect;

public final class SQLExistsExpr extends SQLExprImpl implements Serializable {

    private static final long serialVersionUID = 1L;
    public boolean            not              = false;
    public SQLSelect subQuery;

    public SQLExistsExpr(){

    }

    public SQLExistsExpr(SQLSelect subQuery){
        this.setSubQuery(subQuery);
    }

    public SQLExistsExpr(SQLSelect subQuery, boolean not){
        this.setSubQuery(subQuery);
        this.not = not;
    }

    public boolean isNot() {
        return this.not;
    }

    public void setNot(boolean not) {
        this.not = not;
    }

    public SQLSelect getSubQuery() {
        return this.subQuery;
    }

    public void setSubQuery(SQLSelect subQuery) {
        if (subQuery != null) {
            subQuery.setParent(this);
        }
        this.subQuery = subQuery;
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.subQuery);
        }

        visitor.endVisit(this);
    }

    @Override
    public List getChildren() {
        return Collections.singletonList(this.subQuery);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (not ? 1231 : 1237);
        result = prime * result + ((subQuery == null) ? 0 : subQuery.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SQLExistsExpr other = (SQLExistsExpr) obj;
        if (not != other.not) {
            return false;
        }
        if (subQuery == null) {
            if (other.subQuery != null) {
                return false;
            }
        } else if (!subQuery.equals(other.subQuery)) {
            return false;
        }
        return true;
    }

    public SQLExistsExpr clone () {
        SQLExistsExpr x = new SQLExistsExpr();

        x.not = not;
        if (subQuery != null) {
            x.setSubQuery(subQuery.clone());
        }

        return x;
    }
}
