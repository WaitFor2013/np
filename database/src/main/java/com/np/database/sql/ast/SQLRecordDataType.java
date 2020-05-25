
package com.np.database.sql.ast;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.statement.SQLColumnDefinition;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.statement.SQLColumnDefinition;

public class SQLRecordDataType extends SQLDataTypeImpl implements SQLDataType {
    private final List<SQLColumnDefinition> columns = new ArrayList<SQLColumnDefinition>();

    public List<SQLColumnDefinition> getColumns() {
        return columns;
    }

    public void addColumn(SQLColumnDefinition column) {
        column.setParent(this);
        this.columns.add(column);
    }

    public SQLRecordDataType clone() {
        SQLRecordDataType x = new SQLRecordDataType();
        cloneTo(x);

        for (SQLColumnDefinition c : columns) {
            SQLColumnDefinition c2 = c.clone();
            c2.setParent(x);
            x.columns.add(c2);
        }

        return x;
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.columns);
        }

        visitor.endVisit(this);
    }
}
