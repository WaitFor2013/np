
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLScriptCommitStatement extends SQLStatementImpl {

    public SQLScriptCommitStatement(){

    }

    public void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
}
