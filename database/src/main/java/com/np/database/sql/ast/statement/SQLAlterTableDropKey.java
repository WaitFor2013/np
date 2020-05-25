
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLAlterTableDropKey extends SQLObjectImpl implements SQLAlterTableItem {

    private SQLName keyName;

    public SQLName getKeyName() {
        return keyName;
    }

    public void setKeyName(SQLName keyName) {
        this.keyName = keyName;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if(visitor.visit(this)) {
            acceptChild(visitor, keyName);
        }
        visitor.endVisit(this);
    }

}
