
package com.np.database.sql.dialect.oracle.ast.expr;

import java.util.Arrays;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public class OracleRangeExpr extends OracleSQLObjectImpl implements SQLExpr {

    private SQLExpr lowBound;
    private SQLExpr upBound;

    public OracleRangeExpr(){

    }

    public OracleRangeExpr(SQLExpr lowBound, SQLExpr upBound){
        setLowBound(lowBound);
        setUpBound(upBound);
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, lowBound);
            acceptChild(visitor, upBound);
        }
        visitor.endVisit(this);
    }

    public List<SQLObject> getChildren() {
        return Arrays.<SQLObject>asList(this.lowBound, this.upBound);
    }

    public SQLExpr getLowBound() {
        return lowBound;
    }

    public void setLowBound(SQLExpr lowBound) {
        if (lowBound != null) {
            lowBound.setParent(this);
        }
        this.lowBound = lowBound;
    }

    public SQLExpr getUpBound() {
        return upBound;
    }

    public void setUpBound(SQLExpr upBound) {
        if (upBound != null) {
            upBound.setParent(this);
        }
        this.upBound = upBound;
    }


    public OracleRangeExpr clone() {
        OracleRangeExpr x = new OracleRangeExpr();

        if (lowBound != null) {
            x.setLowBound(lowBound.clone());
        }

        if (upBound != null) {
            x.setUpBound(upBound.clone());
        }

        return x;
    }
}
