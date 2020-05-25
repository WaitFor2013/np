
package com.np.database.sql.dialect.postgresql.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.statement.SQLSelectQuery;
import com.np.database.sql.dialect.postgresql.ast.PGSQLObjectImpl;
import com.np.database.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.statement.SQLSelectQuery;
import com.np.database.sql.dialect.postgresql.visitor.PGASTVisitor;

public class PGValuesQuery extends PGSQLObjectImpl implements SQLSelectQuery {
    private boolean          bracket  = false;

    private List<SQLExpr> values = new ArrayList<SQLExpr>();

    public List<SQLExpr> getValues() {
        return values;
    }

    @Override
    public void accept0(PGASTVisitor visitor) {
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

    public PGValuesQuery clone() {
        PGValuesQuery x = new PGValuesQuery();
        x.bracket = bracket;

        for (int i = 0; i < values.size(); ++i) {
            SQLExpr value = values.get(i).clone();
            value.setParent(x);
            x.values.add(value);
        }

        return x;
    }
}
