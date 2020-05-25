
package com.np.database.sql.dialect.mysql.ast.statement;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.statement.SQLAlterTableItem;
import com.np.database.sql.ast.statement.SQLColumnDefinition;
import com.np.database.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.statement.SQLAlterTableItem;
import com.np.database.sql.ast.statement.SQLColumnDefinition;
import com.np.database.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlAlterTableChangeColumn extends MySqlObjectImpl implements SQLAlterTableItem {

    private SQLName columnName;

    private SQLColumnDefinition newColumnDefinition;

    private boolean             first;

    private SQLName             firstColumn;
    private SQLName             afterColumn;

    @Override
    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, columnName);
            acceptChild(visitor, newColumnDefinition);

            acceptChild(visitor, firstColumn);
            acceptChild(visitor, afterColumn);
        }
    }

    public SQLName getFirstColumn() {
        return firstColumn;
    }

    public void setFirstColumn(SQLName firstColumn) {
        this.firstColumn = firstColumn;
    }

    public SQLName getAfterColumn() {
        return afterColumn;
    }

    public void setAfterColumn(SQLName afterColumn) {
        this.afterColumn = afterColumn;
    }

    public SQLName getColumnName() {
        return columnName;
    }

    public void setColumnName(SQLName columnName) {
        this.columnName = columnName;
    }

    public SQLColumnDefinition getNewColumnDefinition() {
        return newColumnDefinition;
    }

    public void setNewColumnDefinition(SQLColumnDefinition newColumnDefinition) {
        this.newColumnDefinition = newColumnDefinition;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

}
