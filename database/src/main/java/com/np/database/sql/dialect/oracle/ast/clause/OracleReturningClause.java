
package com.np.database.sql.dialect.oracle.ast.clause;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public class OracleReturningClause extends OracleSQLObjectImpl {

    private List<SQLExpr> items  = new ArrayList<SQLExpr>();
    private List<SQLExpr> values = new ArrayList<SQLExpr>();

    public List<SQLExpr> getItems() {
        return items;
    }

    public void addItem(SQLExpr item) {
        if (item != null) {
            item.setParent(this);
        }
        this.items.add(item);
    }

    public List<SQLExpr> getValues() {
        return values;
    }

    public void addValue(SQLExpr value) {
        if (value != null) {
            value.setParent(this);
        }
        this.values.add(value);
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, items);
            acceptChild(visitor, values);
        }
        visitor.endVisit(this);
    }

    public OracleReturningClause clone() {
        OracleReturningClause x = new OracleReturningClause();

        for (SQLExpr item : items) {
            SQLExpr item2 = item.clone();
            item2.setParent(x);
            x.items.add(item2);
        }

        for (SQLExpr v : values) {
            SQLExpr v2 = v.clone();
            v2.setParent(x);
            x.values.add(v2);
        }

        return x;
    }
}
