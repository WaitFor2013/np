
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.ast.expr.SQLListExpr;
import com.np.database.sql.visitor.SQLASTVisitor;

import java.util.ArrayList;
import java.util.List;

public class SQLValuesQuery
        extends SQLObjectImpl implements SQLSelectQuery {
    private boolean          bracket  = false;

    private List<SQLExpr> values = new ArrayList<SQLExpr>();

    public List<SQLExpr> getValues() {
        return values;
    }

    public void addValue(SQLListExpr value) {
        value.setParent(this);
        values.add(value);
    }

    @Override
    public void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, values);
        }
        visitor.endVisit(this);
    }

    @Override
    public boolean isBracket() {
        return bracket;
    }

    @Override
    public void setBracket(boolean bracket) {
        this.bracket = bracket;
    }

    public SQLValuesQuery clone() {
        SQLValuesQuery x = new SQLValuesQuery();
        x.bracket = bracket;

        for (int i = 0; i < values.size(); ++i) {
            SQLExpr value = values.get(i).clone();
            value.setParent(x);
            x.values.add(value);
        }

        return x;
    }
}
