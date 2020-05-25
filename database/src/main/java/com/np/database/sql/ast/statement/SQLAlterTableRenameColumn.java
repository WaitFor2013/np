
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLAlterTableRenameColumn extends SQLObjectImpl implements SQLAlterTableItem {

    private SQLName column;
    private SQLName to;

    public SQLAlterTableRenameColumn(){

    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, column);
            acceptChild(visitor, to);
        }
        visitor.endVisit(this);
    }

    public SQLName getColumn() {
        return column;
    }

    public void setColumn(SQLName column) {
        if (column != null) {
            column.setParent(this);
        }
        this.column = column;
    }

    public SQLName getTo() {
        return to;
    }

    public void setTo(SQLName to) {
        if (to != null) {
            to.setParent(this);
        }
        this.to = to;
    }

}
