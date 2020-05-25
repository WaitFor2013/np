
package com.np.database.sql.dialect.mysql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.statement.SQLAlterStatement;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.statement.SQLAlterStatement;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlAlterServerStatement extends MySqlStatementImpl implements SQLAlterStatement {
    private SQLName name;

    // options
    private SQLExpr user;

    public SQLName getName() {
        return name;
    }

    public void setName(SQLName name) {
        if (name != null) {
            name.setParent(this);
        }
        this.name = name;
    }

    public SQLExpr getUser() {
        return user;
    }

    public void setUser(SQLExpr user) {
        if (user != null) {
            user.setParent(this);
        }
        this.user = user;
    }

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, name);
            acceptChild(visitor, user);
        }
        visitor.endVisit(this);
    }
}
