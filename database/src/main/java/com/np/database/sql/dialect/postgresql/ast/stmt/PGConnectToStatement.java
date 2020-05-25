
package com.np.database.sql.dialect.postgresql.ast.stmt;

import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.np.database.sql.visitor.SQLASTOutputVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.visitor.SQLASTOutputVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;

public class PGConnectToStatement extends SQLStatementImpl implements PGSQLStatement {
    private SQLName target;

    public PGConnectToStatement() {
        super(JdbcConstants.POSTGRESQL);
    }

    protected void accept0(SQLASTVisitor visitor) {
        this.accept0((PGASTVisitor) visitor);
    }

    public void output(StringBuffer buf) {
        SQLASTOutputVisitor visitor = NpSqlHelper.createOutputVisitor(buf, dbType);
        this.accept(visitor);
    }

    @Override
    public void accept0(PGASTVisitor v) {
        if (v.visit(this)) {
            acceptChild(v, target);
        }
        v.endVisit(this);
    }

    public SQLName getTarget() {
        return target;
    }

    public void setTarget(SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.target = x;
    }
}
