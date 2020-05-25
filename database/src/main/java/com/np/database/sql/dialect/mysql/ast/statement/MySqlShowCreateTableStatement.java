
package com.np.database.sql.dialect.mysql.ast.statement;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlShowCreateTableStatement extends MySqlStatementImpl implements MySqlShowStatement {

    private SQLName name;

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, name);
        }
        visitor.endVisit(this);
    }

    public SQLName getName() {
        return name;
    }

    public void setName(SQLName name) {
        if (name != null) {
            name.setParent(this);
        }
        this.name = name;
    }

}
