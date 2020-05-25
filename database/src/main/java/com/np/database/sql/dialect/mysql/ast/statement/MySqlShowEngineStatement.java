
package com.np.database.sql.dialect.mysql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlShowEngineStatement extends MySqlStatementImpl implements MySqlShowStatement {

    private SQLExpr name;
    private Option  option;

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, name);
        }
        visitor.endVisit(this);
    }

    public SQLExpr getName() {
        return name;
    }

    public void setName(SQLExpr name) {
        this.name = name;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public static enum Option {
        STATUS, MUTEX
    }
}
