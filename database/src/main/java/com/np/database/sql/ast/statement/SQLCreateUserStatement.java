
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLCreateUserStatement extends SQLStatementImpl {
    private SQLName user;
    private SQLExpr password;

    // oracle
    private SQLName defaultTableSpace;

    public SQLCreateUserStatement() {

    }

    public SQLName getUser() {
        return user;
    }

    public void setUser(SQLName user) {
        if (user != null) {
            user.setParent(this);
        }
        this.user = user;
    }

    public SQLExpr getPassword() {
        return password;
    }

    public void setPassword(SQLExpr password) {
        if (password != null) {
            password.setParent(this);
        }
        this.password = password;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, user);
            acceptChild(visitor, password);
        }
        visitor.endVisit(this);
    }
}
