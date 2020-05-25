
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLDropUserStatement extends SQLStatementImpl implements SQLDropStatement {

    private List<SQLExpr> users = new ArrayList<SQLExpr>(2);
    
    public SQLDropUserStatement() {
        
    }
    
    public SQLDropUserStatement(String dbType) {
        super (dbType);
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

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, users);
        }
        visitor.endVisit(this);
    }

    @Override
    public List getChildren() {
        return users;
    }
}
