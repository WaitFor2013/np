
package com.np.database.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.statement.SQLColumnDefinition;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.statement.SQLColumnDefinition;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public class OracleAlterTableModify extends OracleAlterTableItem {

    private List<SQLColumnDefinition> columns = new ArrayList<SQLColumnDefinition>();

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, columns);
        }
        visitor.endVisit(this);
    }

    public List<SQLColumnDefinition> getColumns() {
        return columns;
    }

    public void addColumn(SQLColumnDefinition column) {
        if (column != null) {
            column.setParent(this);
        }
        this.columns.add(column);
    }

}
