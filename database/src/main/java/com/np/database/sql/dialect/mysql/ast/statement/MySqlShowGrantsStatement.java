
package com.np.database.sql.dialect.mysql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlShowGrantsStatement extends MySqlStatementImpl implements MySqlShowStatement {

    private SQLExpr user;

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, user);
        }
        visitor.endVisit(this);
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

}
