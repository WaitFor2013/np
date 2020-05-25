
package com.np.database.sql.dialect.oracle.ast;

import com.np.database.sql.ast.SQLDataTypeImpl;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.SQLDataTypeImpl;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;

public class OracleDataTypeIntervalYear extends SQLDataTypeImpl implements OracleSQLObject {

    public OracleDataTypeIntervalYear(){
        this.setName("INTERVAL YEAR");
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor) visitor);
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, getArguments());
        }
        visitor.endVisit(this);
    }

    public OracleDataTypeIntervalYear clone() {
        OracleDataTypeIntervalYear x = new OracleDataTypeIntervalYear();

        super.cloneTo(x);

        return x;
    }
}
