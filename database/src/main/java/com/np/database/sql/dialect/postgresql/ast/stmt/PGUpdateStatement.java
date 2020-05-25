
package com.np.database.sql.dialect.postgresql.ast.stmt;

import com.np.database.sql.ast.statement.SQLUpdateStatement;
import com.np.database.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.ast.statement.SQLUpdateStatement;
import com.np.database.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.visitor.SQLASTVisitor;

public class PGUpdateStatement extends SQLUpdateStatement implements PGSQLStatement {

    private boolean        only      = false;

    public PGUpdateStatement(){
        super (JdbcConstants.POSTGRESQL);
    }

    public boolean isOnly() {
        return only;
    }

    public void setOnly(boolean only) {
        this.only = only;
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof PGASTVisitor) {
            accept0((PGASTVisitor) visitor);
            return;
        }

        super.accept0(visitor);
    }

    @Override
    public void accept0(PGASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, with);
            acceptChild(visitor, tableSource);
            acceptChild(visitor, items);
            acceptChild(visitor, where);
        }
        visitor.endVisit(this);
    }

}
