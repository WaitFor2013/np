
package com.np.database.sql.ast.expr;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLArrayExpr extends SQLExprImpl {

    private SQLExpr       expr;

    private List<SQLExpr> values = new ArrayList<SQLExpr>();

    public SQLArrayExpr clone() {
        SQLArrayExpr x = new SQLArrayExpr();
        if (expr != null) {
            x.setExpr(expr.clone());
        }
        for (SQLExpr value : values) {
            SQLExpr value2 = value.clone();
            value2.setParent(x);
            x.values.add(value2);
        }
        return x;
    }

    public SQLExpr getExpr() {
        return expr;
    }

    public void setExpr(SQLExpr expr) {
        this.expr = expr;
    }

    public List<SQLExpr> getValues() {
        return values;
    }

    public void setValues(List<SQLExpr> values) {
        this.values = values;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, expr);
            acceptChild(visitor, values);
        }
        visitor.endVisit(this);
    }

    public List<SQLObject> getChildren() {
        List<SQLObject> children = new ArrayList<SQLObject>();
        children.add(this.expr);
        children.addAll(this.values);
        return children;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((expr == null) ? 0 : expr.hashCode());
        result = prime * result + ((values == null) ? 0 : values.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SQLArrayExpr other = (SQLArrayExpr) obj;
        if (expr == null) {
            if (other.expr != null) return false;
        } else if (!expr.equals(other.expr)) return false;
        if (values == null) {
            if (other.values != null) return false;
        } else if (!values.equals(other.values)) return false;
        return true;
    }



}
