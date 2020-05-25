
package com.np.database.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLHint;
import com.np.database.sql.ast.statement.SQLErrorLoggingClause;
import com.np.database.sql.ast.statement.SQLInsertStatement;
import com.np.database.sql.dialect.oracle.ast.clause.OracleReturningClause;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.dialect.oracle.visitor.OracleOutputVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.ast.SQLHint;
import com.np.database.sql.ast.statement.SQLErrorLoggingClause;
import com.np.database.sql.ast.statement.SQLInsertStatement;
import com.np.database.sql.dialect.oracle.ast.clause.OracleReturningClause;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.dialect.oracle.visitor.OracleOutputVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.visitor.SQLASTVisitor;

public class OracleInsertStatement extends SQLInsertStatement implements OracleStatement {

    private OracleReturningClause returning;
    private SQLErrorLoggingClause errorLogging;
    private List<SQLHint>         hints = new ArrayList<SQLHint>();

    public OracleInsertStatement() {
        dbType = JdbcConstants.ORACLE;
    }

    public void cloneTo(OracleInsertStatement x) {
        super.cloneTo(x);
        if (returning != null) {
            x.setReturning(returning.clone());
        }
        if (errorLogging != null) {
            x.setErrorLogging(errorLogging.clone());
        }
        for (SQLHint hint : hints) {
            SQLHint h2 = hint.clone();
            h2.setParent(x);
            x.hints.add(h2);
        }
    }

    public List<SQLHint> getHints() {
        return hints;
    }

    public void setHints(List<SQLHint> hints) {
        this.hints = hints;
    }

    public OracleReturningClause getReturning() {
        return returning;
    }

    public void setReturning(OracleReturningClause returning) {
        this.returning = returning;
    }

    public SQLErrorLoggingClause getErrorLogging() {
        return errorLogging;
    }

    public void setErrorLogging(SQLErrorLoggingClause errorLogging) {
        this.errorLogging = errorLogging;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor) visitor);
    }

    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, getTableSource());
            this.acceptChild(visitor, getColumns());
            this.acceptChild(visitor, getValues());
            this.acceptChild(visitor, getQuery());
            this.acceptChild(visitor, returning);
            this.acceptChild(visitor, errorLogging);
        }

        visitor.endVisit(this);
    }
    
    public void output(StringBuffer buf) {
    	new OracleOutputVisitor(buf).visit(this);
    }

    public OracleInsertStatement clone() {
        OracleInsertStatement x = new OracleInsertStatement();
        cloneTo(x);
        return x;
    }
}
