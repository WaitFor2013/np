
package com.np.database.sql.dialect.mysql.ast.statement;

import com.np.database.sql.ast.SQLLimit;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.SQLLimit;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlShowErrorsStatement extends MySqlStatementImpl implements MySqlShowStatement {

    private boolean count = false;
    private SQLLimit limit;

    public boolean isCount() {
        return count;
    }

    public void setCount(boolean count) {
        this.count = count;
    }

    public SQLLimit getLimit() {
        return limit;
    }

    public void setLimit(SQLLimit limit) {
        this.limit = limit;
    }

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, limit);
        }
        visitor.endVisit(this);
    }
}
