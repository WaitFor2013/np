
package com.np.database.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlKillStatement extends MySqlStatementImpl {

    private Type          type;
    private List<SQLExpr> threadIds = new ArrayList<SQLExpr>();

    public static enum Type {
                             CONNECTION, QUERY
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public SQLExpr getThreadId() {
        return threadIds.get(0);
    }

    public void setThreadId(SQLExpr threadId) {
        this.threadIds.set(0, threadId);
    }
    
    public List<SQLExpr> getThreadIds() {
        return threadIds;
    }

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, threadIds);
        }
        visitor.endVisit(this);
    }

    @Override
    public List<SQLObject> getChildren() {
        return Collections.<SQLObject>emptyList();
    }
}
