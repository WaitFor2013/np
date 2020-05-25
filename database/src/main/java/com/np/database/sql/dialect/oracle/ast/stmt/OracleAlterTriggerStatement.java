
package com.np.database.sql.dialect.oracle.ast.stmt;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public class OracleAlterTriggerStatement extends OracleStatementImpl implements OracleAlterStatement {

    private SQLName name;

    private Boolean enable;

    private boolean compile;

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, name);
        }
        visitor.endVisit(this);
    }

    public boolean isCompile() {
        return compile;
    }

    public void setCompile(boolean compile) {
        this.compile = compile;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public SQLName getName() {
        return name;
    }

    public void setName(SQLName name) {
        this.name = name;
    }

}
