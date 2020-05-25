
package com.np.database.sql.ast.statement;

import java.util.List;

import com.np.database.sql.ast.SQLName;

public interface SQLForeignKeyConstraint extends SQLConstraint, SQLTableElement, SQLTableConstraint {

    List<SQLName> getReferencingColumns();

    SQLExprTableSource getReferencedTable();
    SQLName getReferencedTableName();

    void setReferencedTableName(SQLName value);

    List<SQLName> getReferencedColumns();
}
