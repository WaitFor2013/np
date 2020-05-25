
package com.np.database.sql.dialect.postgresql.ast.stmt;

import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.visitor.SQLASTVisitor;

public class PGStartTransactionStatement extends SQLStatementImpl implements PGSQLStatement {
    public PGStartTransactionStatement() {
        super(JdbcConstants.POSTGRESQL);
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof PGASTVisitor) {
            accept0((PGASTVisitor) visitor);
        }
    }

    @Override
    public void accept0(PGASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
}
