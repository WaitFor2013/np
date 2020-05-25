
package com.np.database.sql.dialect.oracle.ast.stmt;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public class OracleGotoStatement extends OracleStatementImpl {

    private SQLName label;

    public OracleGotoStatement(){
    }

    public OracleGotoStatement(SQLName label){
        this.label = label;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, label);
        }
        visitor.endVisit(this);
    }

    public SQLName getLabel() {
        return label;
    }

    public void setLabel(SQLName label) {
        this.label = label;
    }

}
