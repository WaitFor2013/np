
package com.np.database.sql.dialect.mysql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlHelpStatement extends MySqlStatementImpl {

    private SQLExpr content;

    public SQLExpr getContent() {
        return content;
    }

    public void setContent(SQLExpr content) {
        this.content = content;
    }

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, content);
        }
        visitor.endVisit(this);
    }
}
