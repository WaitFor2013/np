
package com.np.database.sql.dialect.oracle.ast.stmt;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.statement.SQLDropStatement;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.statement.SQLDropStatement;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public class OracleDropDbLinkStatement extends OracleStatementImpl implements SQLDropStatement {

    private boolean isPublic;

    private SQLName name;

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean value) {
        this.isPublic = value;
    }

    public SQLName getName() {
        return name;
    }

    public void setName(SQLName name) {
        this.name = name;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, name);
        }
        visitor.endVisit(this);
    }

}
