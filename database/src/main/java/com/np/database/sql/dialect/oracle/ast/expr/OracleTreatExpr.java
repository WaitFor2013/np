
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

public class OracleTreatExpr extends OracleSQLObjectImpl implements SQLExpr {

    private SQLExpr expr;
    private SQLExpr type;
    private boolean ref;

    public SQLExpr getExpr() {
        return expr;
    }

    public void setExpr(SQLExpr expr) {
        if (expr != null) {
            expr.setParent(this);
        }
        this.expr = expr;
    }

    public SQLExpr getType() {
        return type;
    }

    public void setType(SQLExpr type) {
        if (type != null) {
            type.setParent(this);
        }
        this.type = type;
    }

    public boolean isRef() {
        return ref;
    }

    public void setRef(boolean ref) {
        this.ref = ref;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, expr);
            acceptChild(visitor, type);
        }
        visitor.endVisit(this);
    }

    public List<SQLObject> getChildren() {
        return Arrays.<SQLObject>asList(this.expr, this.type);
    }

    public OracleTreatExpr clone() {
        OracleTreatExpr x = new OracleTreatExpr();

        if (expr != null) {
            x.setExpr(expr.clone());
        }

        if (type != null) {
            x.setType(type.clone());
        }

        x.ref = ref;

        return x;
    }
}
