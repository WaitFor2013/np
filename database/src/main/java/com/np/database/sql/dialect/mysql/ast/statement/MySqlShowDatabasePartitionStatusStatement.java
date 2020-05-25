
package com.np.database.sql.dialect.mysql.ast.statement;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlShowDatabasePartitionStatusStatement extends MySqlStatementImpl implements MySqlShowStatement {

    private SQLName database;

    public SQLName getDatabase() {
        return database;
    }

    public void setDatabase(SQLName database) {
        this.database = database;
    }

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, database);
        }
        visitor.endVisit(this);
    }
}
