
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

public class OracleDatetimeExpr extends OracleSQLObjectImpl implements SQLExpr {

    private SQLExpr expr;
    private SQLExpr timeZone;

    public OracleDatetimeExpr(){

    }

    public OracleDatetimeExpr(SQLExpr expr, SQLExpr timeZone){
        this.expr = expr;
        this.timeZone = timeZone;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, expr);
            acceptChild(visitor, timeZone);
        }
        visitor.endVisit(this);
    }

    public SQLExpr getExpr() {
        return expr;
    }

    public void setExpr(SQLExpr expr) {
        this.expr = expr;
    }

    public SQLExpr getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(SQLExpr timeZone) {
        this.timeZone = timeZone;
    }

    public OracleDatetimeExpr clone() {
        OracleDatetimeExpr x = new OracleDatetimeExpr();

        if (expr != null) {
            x.setExpr(expr.clone());
        }

        if (timeZone != null) {
            x.setTimeZone(timeZone.clone());
        }

        return x;
    }

    @Override
    public List<SQLObject> getChildren() {
        return Arrays.<SQLObject>asList(this.expr, this.timeZone);
    }
}
