
package com.np.database.sql.dialect.oracle.ast.stmt;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public class OracleExitStatement extends OracleStatementImpl {
    private String label;

    private SQLExpr when;

    public SQLExpr getWhen() {
        return when;
    }

    public void setWhen(SQLExpr when) {
        if (when != null) {
            when.setParent(this);
        }
        this.when = when;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, when);
        }
        visitor.endVisit(this);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
