
package com.np.database.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLHint;
import com.np.database.sql.ast.statement.SQLDeleteStatement;
import com.np.database.sql.dialect.oracle.ast.clause.OracleReturningClause;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.ast.SQLHint;
import com.np.database.sql.ast.statement.SQLDeleteStatement;
import com.np.database.sql.dialect.oracle.ast.clause.OracleReturningClause;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.visitor.SQLASTVisitor;

public class OracleDeleteStatement extends SQLDeleteStatement {



    private final List<SQLHint>   hints     = new ArrayList<SQLHint>();
    private OracleReturningClause returning = null;

    public OracleDeleteStatement(){
        super (JdbcConstants.ORACLE);
    }

    public OracleReturningClause getReturning() {
        return returning;
    }

    public void setReturning(OracleReturningClause returning) {
        this.returning = returning;
    }

    public List<SQLHint> getHints() {
        return this.hints;
    }

    protected void accept0(SQLASTVisitor visitor) {
        accept0((OracleASTVisitor) visitor);
    }

    protected void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.hints);
            acceptChild(visitor, this.tableSource);
            acceptChild(visitor, this.getWhere());
            acceptChild(visitor, returning);
        }

        visitor.endVisit(this);
    }

    public OracleDeleteStatement clone() {
        OracleDeleteStatement x = new OracleDeleteStatement();
        cloneTo(x);

        for (SQLHint hint : hints) {
            SQLHint hint2 = hint.clone();
            hint2.setParent(x);
            x.hints.add(hint2);
        }
        if (returning != null) {
            x.setReturning(returning.clone());
        }

        return x;
    }

}
