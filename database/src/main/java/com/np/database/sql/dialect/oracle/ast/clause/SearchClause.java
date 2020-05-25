
package com.np.database.sql.dialect.oracle.ast.clause;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.expr.SQLIdentifierExpr;
import com.np.database.sql.ast.statement.SQLSelectOrderByItem;
import com.np.database.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.expr.SQLIdentifierExpr;
import com.np.database.sql.ast.statement.SQLSelectOrderByItem;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public class SearchClause extends OracleSQLObjectImpl {

    public static enum Type {
        DEPTH, BREADTH
    }

    private Type                          type;

    private final List<SQLSelectOrderByItem> items = new ArrayList<SQLSelectOrderByItem>();

    private SQLIdentifierExpr orderingColumn;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<SQLSelectOrderByItem> getItems() {
        return items;
    }
    
    public void addItem(SQLSelectOrderByItem item) {
        if (item != null) {
            item.setParent(this);
        }
        this.items.add(item);
    }

    public SQLIdentifierExpr getOrderingColumn() {
        return orderingColumn;
    }

    public void setOrderingColumn(SQLIdentifierExpr orderingColumn) {
        if (orderingColumn != null) {
            orderingColumn.setParent(this);
        }
        this.orderingColumn = orderingColumn;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, items);
            acceptChild(visitor, orderingColumn);
        }
        visitor.endVisit(this);
    }

    public SearchClause clone() {
        SearchClause x = new SearchClause();

        x.type = type;

        for (SQLSelectOrderByItem item : items) {
            SQLSelectOrderByItem item2 = item.clone();
            item2.setParent(x);
            x.items.add(item2);
        }

        if (orderingColumn != null) {
            x.setOrderingColumn(orderingColumn.clone());
        }

        return x;
    }
}
