
package com.np.database.sql.dialect.mysql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlShowCreateEventStatement extends MySqlStatementImpl implements MySqlShowStatement {

    private SQLExpr eventName;

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, eventName);
        }
        visitor.endVisit(this);
    }

    public SQLExpr getEventName() {
        return eventName;
    }

    public void setEventName(SQLExpr eventName) {
        this.eventName = eventName;
    }

}
