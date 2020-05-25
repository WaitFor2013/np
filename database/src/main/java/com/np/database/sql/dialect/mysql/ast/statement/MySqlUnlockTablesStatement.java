
package com.np.database.sql.dialect.mysql.ast.statement;

import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlUnlockTablesStatement extends MySqlStatementImpl {

    public void accept0(MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
}
