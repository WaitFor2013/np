
package com.np.database.sql.dialect.oracle.ast.stmt;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public class OracleContinueStatement extends OracleStatementImpl {

    private SQLExpr when;
    private String label;

    public SQLExpr getWhen() {
        return when;
    }

    public void setWhen(SQLExpr when) {
        if (when != null) {
            when.setParent(this);
        }
        this.when = when;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, when);
        }
        visitor.endVisit(this);
    }

}
