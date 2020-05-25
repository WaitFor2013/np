
package com.np.database.sql.dialect.oracle.ast.stmt;

import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.visitor.SQLASTVisitor;

public abstract class OracleStatementImpl extends SQLStatementImpl implements OracleStatement {
    
    public OracleStatementImpl() {
        super(JdbcConstants.ORACLE);
    }

    protected void accept0(SQLASTVisitor visitor) {
        accept0((OracleASTVisitor) visitor);
    }

    public abstract void accept0(OracleASTVisitor visitor);

    public String toString() {
        return NpSqlHelper.toOracleString(this);
    }
}
