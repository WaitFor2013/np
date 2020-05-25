
package com.np.database.sql.dialect.mysql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLLimit;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlShowRelayLogEventsStatement extends MySqlStatementImpl implements MySqlShowStatement {

    private SQLExpr logName;
    private SQLExpr from;
    private SQLLimit limit;

    public SQLExpr getLogName() {
        return logName;
    }

    public void setLogName(SQLExpr logName) {
        this.logName = logName;
    }

    public SQLExpr getFrom() {
        return from;
    }

    public void setFrom(SQLExpr from) {
        this.from = from;
    }

    public SQLLimit getLimit() {
        return limit;
    }

    public void setLimit(SQLLimit limit) {
        this.limit = limit;
    }

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, logName);
            acceptChild(visitor, from);
            acceptChild(visitor, limit);
        }
        visitor.endVisit(this);
    }
}
