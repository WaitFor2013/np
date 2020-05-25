
package com.np.database.sql.dialect.oracle.ast.expr;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public class OracleSizeExpr extends OracleSQLObjectImpl implements OracleExpr {

    private SQLExpr value;
    private Unit    unit;

    public OracleSizeExpr(){

    }

    public OracleSizeExpr(SQLExpr value, Unit unit){
        super();
        this.value = value;
        this.unit = unit;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, value);
        }
        visitor.endVisit(this);
    }

    public List<SQLObject> getChildren() {
        return Collections.<SQLObject>singletonList(value);
    }

    public SQLExpr getValue() {
        return value;
    }

    public void setValue(SQLExpr value) {
        this.value = value;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public static enum Unit {
        K, M, G, T, P, E
    }

    public OracleSizeExpr clone() {
        OracleSizeExpr x = new OracleSizeExpr();

        if (value != null) {
            x.setValue(value.clone());
        }
        x.unit = unit;

        return x;
    }
}
