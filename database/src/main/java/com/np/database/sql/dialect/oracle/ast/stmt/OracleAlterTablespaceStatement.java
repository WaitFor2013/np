
package com.np.database.sql.dialect.oracle.ast.stmt;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public class OracleAlterTablespaceStatement extends OracleStatementImpl implements OracleAlterStatement {

    private SQLName name;
    private OracleAlterTablespaceItem item;

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, name);
            acceptChild(visitor, item);
        }
        visitor.endVisit(this);
    }

    public SQLName getName() {
        return name;
    }

    public void setName(SQLName name) {
        this.name = name;
    }

    public OracleAlterTablespaceItem getItem() {
        return item;
    }

    public void setItem(OracleAlterTablespaceItem item) {
        this.item = item;
    }

}
