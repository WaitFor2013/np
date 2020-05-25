
package com.np.database.sql.ast.expr;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLDataType;
import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.statement.SQLSelect;
import com.np.database.sql.ast.statement.SQLSelectItem;
import com.np.database.sql.ast.statement.SQLSelectQueryBlock;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.statement.SQLSelect;
import com.np.database.sql.ast.statement.SQLSelectItem;
import com.np.database.sql.ast.statement.SQLSelectQueryBlock;

public class SQLQueryExpr extends SQLExprImpl implements Serializable {

    private static final long serialVersionUID = 1L;
    public SQLSelect subQuery;

    public SQLQueryExpr(){

    }

    public SQLQueryExpr(SQLSelect select){
        setSubQuery(select);
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

    public void output(StringBuffer buf) {
        this.subQuery.output(buf);
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.subQuery);
        }

        visitor.endVisit(this);
    }

    public List<SQLObject> getChildren() {
        return Collections.<SQLObject>singletonList(subQuery);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        SQLQueryExpr other = (SQLQueryExpr) obj;
        if (subQuery == null) {
            if (other.subQuery != null) {
                return false;
            }
        } else if (!subQuery.equals(other.subQuery)) {
            return false;
        }
        return true;
    }

    public SQLQueryExpr clone() {
        SQLQueryExpr x = new SQLQueryExpr();

        if (subQuery != null) {
            x.setSubQuery(subQuery.clone());
        }

        return x;
    }

    public SQLDataType computeDataType() {
        if (subQuery == null) {
            return null;
        }

        SQLSelectQueryBlock queryBlock = subQuery.getFirstQueryBlock();
        if (queryBlock == null) {
            return null;
        }

        List<SQLSelectItem> selectList = queryBlock.getSelectList();
        if (selectList.size() == 1) {
            return selectList.get(0).computeDataType();
        }

        return null;
    }
}
