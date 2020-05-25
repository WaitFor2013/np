
package com.np.database.sql.dialect.oracle.ast;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLDataTypeImpl;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.SQLDataTypeImpl;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;

public class OracleDataTypeIntervalDay extends SQLDataTypeImpl implements OracleSQLObject {

    private boolean               toSecond          = false;

    protected final List<SQLExpr> fractionalSeconds = new ArrayList<SQLExpr>();

    public OracleDataTypeIntervalDay(){
        this.setName("INTERVAL DAY");
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

    public boolean isToSecond() {
        return toSecond;
    }

    public void setToSecond(boolean toSecond) {
        this.toSecond = toSecond;
    }

    public List<SQLExpr> getFractionalSeconds() {
        return fractionalSeconds;
    }

    public OracleDataTypeIntervalDay clone() {
        OracleDataTypeIntervalDay x = new OracleDataTypeIntervalDay();

        super.cloneTo(x);

        for (SQLExpr arg : fractionalSeconds) {
            arg.setParent(x);
            x.fractionalSeconds.add(arg);
        }

        return x;
    }

}
