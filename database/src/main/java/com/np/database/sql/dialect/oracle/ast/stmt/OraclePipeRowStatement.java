
package com.np.database.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public class OraclePipeRowStatement extends OracleStatementImpl {
    private final List<SQLExpr> parameters = new ArrayList<SQLExpr>();

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, parameters);
        }
        visitor.endVisit(this);
    }

    public List<SQLExpr> getParameters() {
        return parameters;
    }
}
