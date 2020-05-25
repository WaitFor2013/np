
package com.np.database.sql.ast.expr;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLBinaryOpExprGroup extends SQLExprImpl {
    private final SQLBinaryOperator operator;
    private final List<SQLExpr> items = new ArrayList<SQLExpr>();
    private String dbType;

    public SQLBinaryOpExprGroup(SQLBinaryOperator operator) {
        this.operator = operator;
    }

    public SQLBinaryOpExprGroup(SQLBinaryOperator operator, String dbType) {
        this.operator = operator;
        this.dbType = dbType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SQLBinaryOpExprGroup that = (SQLBinaryOpExprGroup) o;

        if (operator != that.operator) return false;
        return items.equals(that.items);
    }

    @Override
    public int hashCode() {
        int result = operator != null ? operator.hashCode() : 0;
        result = 31 * result + items.hashCode();
        return result;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.items);
        }

        visitor.endVisit(this);
    }

    @Override
    public SQLExpr clone() {
        SQLBinaryOpExprGroup x = new SQLBinaryOpExprGroup(operator);

        for (SQLExpr item : items) {
            SQLExpr item2 = item.clone();
            item2.setParent(this);
            x.items.add(item2);
        }

        return x;
    }

    @Override
    public List getChildren() {
        return items;
    }

    public void add(SQLExpr item) {
        if (item != null) {
            item.setParent(this);
        }
        this.items.add(item);
    }

    public List<SQLExpr> getItems() {
        return this.items;
    }

    public SQLBinaryOperator getOperator() {
        return operator;
    }

    public String toString() {
        return NpSqlHelper.toSQLString(this, dbType);
    }
}
