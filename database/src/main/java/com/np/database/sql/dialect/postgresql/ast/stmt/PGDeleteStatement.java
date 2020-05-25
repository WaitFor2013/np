
package com.np.database.sql.dialect.postgresql.ast.stmt;

import com.np.database.sql.ast.statement.SQLDeleteStatement;
import com.np.database.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.ast.statement.SQLDeleteStatement;
import com.np.database.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.visitor.SQLASTVisitor;

public class PGDeleteStatement extends SQLDeleteStatement implements PGSQLStatement {

    private boolean       returning;

    public PGDeleteStatement() {
        super (JdbcConstants.POSTGRESQL);
    }

    public boolean isReturning() {
        return returning;
    }

    public void setReturning(boolean returning) {
        this.returning = returning;
    }

    public String getAlias() {
        if (tableSource == null) {
            return null;
        }
        return tableSource.getAlias();
    }

    public void setAlias(String alias) {
        this.tableSource.setAlias(alias);
    }

    protected void accept0(SQLASTVisitor visitor) {
        accept0((PGASTVisitor) visitor);
    }

    @Override
    public void accept0(PGASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, with);
            acceptChild(visitor, tableSource);
            acceptChild(visitor, using);
            acceptChild(visitor, where);
        }

        visitor.endVisit(this);
    }

    public PGDeleteStatement clone() {
        PGDeleteStatement x = new PGDeleteStatement();
        cloneTo(x);

        x.returning = returning;

        return x;
    }
}
