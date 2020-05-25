
package com.np.database.sql.dialect.oracle.ast.stmt;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public class OracleRaiseStatement extends OracleStatementImpl {

    private SQLExpr exception;

    public SQLExpr getException() {
        return exception;
    }

    public void setException(SQLExpr exception) {
        this.exception = exception;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, exception);
        }
        visitor.endVisit(this);
    }

}
