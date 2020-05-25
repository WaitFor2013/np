
package com.np.database.sql.ast.expr;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLValuesExpr extends SQLExprImpl {
    private List<SQLListExpr> values = new ArrayList<SQLListExpr>();

    public List<SQLListExpr> getValues() {
        return values;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, values);
        }
        visitor.endVisit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SQLValuesExpr that = (SQLValuesExpr) o;

        return values.equals(that.values);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    @Override
    public SQLExpr clone() {
        SQLValuesExpr x = new SQLValuesExpr();

        for (SQLListExpr value : values) {
            SQLListExpr value2 = value.clone();
            value2.setParent(x);
            x.values.add(value2);
        }

        return x;
    }

    @Override
    public List getChildren() {
        return values;
    }
}
