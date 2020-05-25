
package com.np.database.sql.dialect.oracle.ast.stmt;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.statement.SQLCheck;
import com.np.database.sql.dialect.oracle.ast.OracleSQLObject;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.statement.SQLCheck;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;

public class OracleCheck extends SQLCheck implements OracleConstraint, OracleSQLObject {

    private OracleUsingIndexClause using;
    private SQLName exceptionsInto;
    private Initially              initially;
    private Boolean                deferrable;

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof OracleASTVisitor) {
            accept0((OracleASTVisitor) visitor);
            return;
        }

        super.accept(visitor);
    }

    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.getName());
            acceptChild(visitor, this.getExpr());

            acceptChild(visitor, using);
            acceptChild(visitor, exceptionsInto);
        }
        visitor.endVisit(this);
    }

    public Boolean getDeferrable() {
        return deferrable;
    }

    public void setDeferrable(Boolean deferrable) {
        this.deferrable = deferrable;
    }

    public Initially getInitially() {
        return initially;
    }

    public void setInitially(Initially initially) {
        this.initially = initially;
    }

    public SQLName getExceptionsInto() {
        return exceptionsInto;
    }

    public void setExceptionsInto(SQLName exceptionsInto) {
        this.exceptionsInto = exceptionsInto;
    }

    public OracleUsingIndexClause getUsing() {
        return using;
    }

    public void setUsing(OracleUsingIndexClause using) {
        if (using != null) {
            using.setParent(this);
        }
        this.using = using;
    }

    public void cloneTo(OracleCheck x) {
        super.cloneTo(x);
        if (using != null) {
            x.setUsing(using.clone());
        }
        if (exceptionsInto != null) {
            x.setExceptionsInto(exceptionsInto.clone());
        }
        x.initially = initially;
        x.deferrable = deferrable;
    }

    public OracleCheck clone() {
        OracleCheck x = new OracleCheck();
        cloneTo(x);
        return x;
    }
}
