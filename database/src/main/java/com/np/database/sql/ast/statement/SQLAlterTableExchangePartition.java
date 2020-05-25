
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLAlterTableExchangePartition extends SQLObjectImpl implements SQLAlterTableItem {
    private SQLName partition;
    private SQLExprTableSource table;
    private Boolean validation;

    public SQLAlterTableExchangePartition() {

    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, partition);
            acceptChild(visitor, table);
        }
        visitor.endVisit(this);
    }

    public SQLName getPartition() {
        return partition;
    }

    public void setPartition(SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.partition = x;
    }

    public SQLExprTableSource getTable() {
        return table;
    }

    public void setTable(SQLName x) {
        setTable(new SQLExprTableSource(x));
    }

    public void setTable(SQLExprTableSource x) {
        if (x != null) {
            x.setParent(this);
        }
        this.table = x;
    }

    public void setValidation(boolean validation) {
        this.validation = validation;
    }

    public Boolean getValidation() {
        return validation;
    }
}
