
package com.np.database.sql.ast.expr;

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

public class SQLAnyExpr extends SQLExprImpl {

    public SQLSelect subQuery;

    public SQLAnyExpr(){

    }

    public SQLAnyExpr(SQLSelect select){
        setSubQuery(select);
    }

    public SQLAnyExpr clone() {
        SQLAnyExpr x = new SQLAnyExpr();
        if (subQuery != null) {
            x.setSubQuery(subQuery.clone());
        }
        return x;
    }

    public SQLSelect getSubQuery() {
        return this.subQuery;
    }

    public void setSubQuery(SQLSelect x) {
        if (x != null) {
            x.setParent(this);
        }
        this.subQuery = x;
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
        return Collections.<SQLObject>singletonList(this.subQuery);
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
        SQLAnyExpr other = (SQLAnyExpr) obj;
        if (subQuery == null) {
            if (other.subQuery != null) {
                return false;
            }
        } else if (!subQuery.equals(other.subQuery)) {
            return false;
        }
        return true;
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
