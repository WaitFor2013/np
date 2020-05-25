
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLDropDatabaseStatement extends SQLStatementImpl implements SQLDropStatement {

    private SQLExpr database;
    private boolean ifExists;
    
    public SQLDropDatabaseStatement() {
        
    }
    
    public SQLDropDatabaseStatement(String dbType) {
        super (dbType);
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, database);
        }
        visitor.endVisit(this);
    }

    public SQLExpr getDatabase() {
        return database;
    }

    public void setDatabase(SQLExpr database) {
        if (database != null) {
            database.setParent(this);
        }
        this.database = database;
    }

    public boolean isIfExists() {
        return ifExists;
    }

    public void setIfExists(boolean ifExists) {
        this.ifExists = ifExists;
    }

    @Override
    public List<SQLObject> getChildren() {
        List<SQLObject> children = new ArrayList<SQLObject>();
        if (database != null) {
            children.add(database);
        }
        return children;
    }

}
