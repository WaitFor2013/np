
package com.np.database.sql.dialect.oracle.ast.stmt;

import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLCharExpr;
import com.np.database.sql.ast.statement.SQLExplainStatement;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLCharExpr;
import com.np.database.sql.ast.statement.SQLExplainStatement;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.visitor.SQLASTVisitor;

public class OracleExplainStatement extends SQLExplainStatement implements OracleStatement {

    private SQLCharExpr statementId;
    private SQLExpr into;
    
    public OracleExplainStatement() {
        super (JdbcConstants.ORACLE);
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, statementId);
            acceptChild(visitor, into);
            acceptChild(visitor, statement);
        }
        visitor.endVisit(this);
    }

    protected void accept0(SQLASTVisitor visitor) {
        accept0((OracleASTVisitor) visitor);
    }

    public String toString() {
        return NpSqlHelper.toOracleString(this);
    }

    public SQLCharExpr getStatementId() {
        return statementId;
    }

    public void setStatementId(SQLCharExpr statementId) {
        this.statementId = statementId;
    }

    public SQLExpr getInto() {
        return into;
    }

    public void setInto(SQLExpr into) {
        this.into = into;
    }

}
