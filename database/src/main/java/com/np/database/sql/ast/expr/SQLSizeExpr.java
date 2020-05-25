
package com.np.database.sql.ast.expr;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.visitor.SQLASTVisitor;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class SQLSizeExpr
        extends SQLExprImpl {

    private SQLExpr value;
    private Unit    unit;

    public SQLSizeExpr(){

    }

    public SQLSizeExpr(String value, char unit){
        this.unit = Unit.valueOf(Character.toString(unit).toUpperCase());
        if (value.indexOf('.') == -1) {
            this.value = new SQLIntegerExpr(Integer.parseInt(value));
        } else {
            this.value = new SQLNumberExpr(new BigDecimal(value));
        }
    }

    public SQLSizeExpr(SQLExpr value, Unit unit){
        super();
        this.value = value;
        this.unit = unit;
    }

    @Override
    public void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.value != null) {
                this.value.accept(visitor);
            }
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
        B, K, M, G, T, P, E
    }

    public SQLSizeExpr clone() {
        SQLSizeExpr x = new SQLSizeExpr();

        if (value != null) {
            x.setValue(value.clone());
        }
        x.unit = unit;

        return x;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SQLSizeExpr that = (SQLSizeExpr) o;

        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return unit == that.unit;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        return result;
    }
}
