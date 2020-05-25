
package com.np.database.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.statement.SQLAlterStatement;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.statement.SQLAlterStatement;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlAlterUserStatement extends MySqlStatementImpl implements SQLAlterStatement {

    private final List<SQLExpr> users = new ArrayList<SQLExpr>();
    
    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, users);
        }
        visitor.endVisit(this);
    }

    public List<SQLExpr> getUsers() {
        return users;
    }

    public void addUser(SQLExpr user) {
        if (user != null) {
            user.setParent(this);
        }
        this.users.add(user);
    }
}
