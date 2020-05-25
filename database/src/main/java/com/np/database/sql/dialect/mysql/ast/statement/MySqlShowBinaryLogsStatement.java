
package com.np.database.sql.dialect.mysql.ast.statement;

import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlShowBinaryLogsStatement extends MySqlStatementImpl implements MySqlShowStatement {

    public void accept0(MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
}
