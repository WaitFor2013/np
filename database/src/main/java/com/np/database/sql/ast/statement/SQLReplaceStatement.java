
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.ast.expr.SQLQueryExpr;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLReplaceStatement extends SQLStatementImpl {
    protected boolean             lowPriority = false;
    protected boolean             delayed     = false;

    protected SQLExprTableSource  tableSource;
    protected final List<SQLExpr> columns     = new ArrayList<SQLExpr>();
    protected List<SQLInsertStatement.ValuesClause>  valuesList  = new ArrayList<SQLInsertStatement.ValuesClause>();
    protected SQLQueryExpr query;


    public SQLName getTableName() {
        if (tableSource == null) {
            return null;
        }

        return (SQLName) tableSource.getExpr();
    }

    public void setTableName(SQLName tableName) {
        this.setTableSource(new SQLExprTableSource(tableName));
    }

    public SQLExprTableSource getTableSource() {
        return tableSource;
    }

    public void setTableSource(SQLExprTableSource tableSource) {
        if (tableSource != null) {
            tableSource.setParent(this);
        }
        this.tableSource = tableSource;
    }

    public List<SQLExpr> getColumns() {
        return columns;
    }

    public void addColumn(SQLExpr column) {
        if (column != null) {
            column.setParent(this);
        }
        this.columns.add(column);
    }

    public boolean isLowPriority() {
        return lowPriority;
    }

    public void setLowPriority(boolean lowPriority) {
        this.lowPriority = lowPriority;
    }

    public boolean isDelayed() {
        return delayed;
    }

    public void setDelayed(boolean delayed) {
        this.delayed = delayed;
    }

    public SQLQueryExpr getQuery() {
        return query;
    }

    public void setQuery(SQLQueryExpr query) {
        if (query != null) {
            query.setParent(this);
        }
        this.query = query;
    }

    public List<SQLInsertStatement.ValuesClause> getValuesList() {
        return valuesList;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, tableSource);
            acceptChild(visitor, columns);
            acceptChild(visitor, valuesList);
            acceptChild(visitor, query);
        }
        visitor.endVisit(this);
    }
}
