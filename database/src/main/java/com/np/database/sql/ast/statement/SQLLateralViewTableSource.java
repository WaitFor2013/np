
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLLateralViewTableSource extends SQLTableSourceImpl {

    private SQLTableSource      tableSource;
    private boolean outer;

    private SQLMethodInvokeExpr method;

    private List<SQLName>       columns = new ArrayList<SQLName>(2);

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, tableSource);
            acceptChild(visitor, method);
            acceptChild(visitor, columns);
        }
        visitor.endVisit(this);
    }

    public SQLTableSource getTableSource() {
        return tableSource;
    }

    public void setTableSource(SQLTableSource tableSource) {
        if (tableSource != null) {
            tableSource.setParent(this);
        }
        this.tableSource = tableSource;
    }

    public SQLMethodInvokeExpr getMethod() {
        return method;
    }

    public void setMethod(SQLMethodInvokeExpr method) {
        if (method != null) {
            method.setParent(this);
        }
        this.method = method;
    }

    public List<SQLName> getColumns() {
        return columns;
    }

    public void setColumns(List<SQLName> columns) {
        this.columns = columns;
    }

    public SQLTableSource findTableSource(long alias_hash) {
        long hash = this.aliasHashCode64();
        if (hash != 0 && hash == alias_hash) {
            return this;
        }

        for (SQLName column : columns) {
            if (column.nameHashCode64() == alias_hash) {
                return this;
            }
        }

        if (tableSource != null) {
            return tableSource.findTableSource(alias_hash);
        }

        return null;
    }

    public SQLTableSource findTableSourceWithColumn(long columnNameHash) {
        for (SQLName column : columns) {
            if (column.nameHashCode64() == columnNameHash) {
                return this;
            }
        }

        if (tableSource != null) {
            return tableSource.findTableSourceWithColumn(columnNameHash);
        }
        return null;
    }

    public boolean isOuter() {
        return outer;
    }

    public void setOuter(boolean outer) {
        this.outer = outer;
    }
}
