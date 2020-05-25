
package com.np.database.sql.dialect.mysql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlShowCreateDatabaseStatement extends MySqlStatementImpl implements MySqlShowStatement {

    private SQLExpr database;

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, database);
        }
        visitor.endVisit(this);
    }

    public SQLExpr getDatabase() {
        return database;
    }

    public void setDatabase(SQLExpr database) {
        this.database = database;
    }

}
