
package com.np.database.sql.dialect.mysql.ast;

import com.np.database.sql.ast.statement.SQLPrimaryKey;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.util.JdbcConstants;

public class MySqlPrimaryKey extends MySqlKey implements SQLPrimaryKey {

    public MySqlPrimaryKey(){
        dbType = JdbcConstants.MYSQL;
    }

    protected void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.getName());
            acceptChild(visitor, this.getColumns());
        }
        visitor.endVisit(this);
    }

    public MySqlPrimaryKey clone() {
        MySqlPrimaryKey x = new MySqlPrimaryKey();
        cloneTo(x);
        return x;
    }
}
