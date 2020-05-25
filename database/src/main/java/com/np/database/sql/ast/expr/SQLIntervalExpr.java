
package com.np.database.sql.ast.expr;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLIntervalExpr extends SQLExprImpl {

    private SQLExpr           value;
    private SQLIntervalUnit   unit;

    public SQLIntervalExpr(){
    }

    public SQLIntervalExpr(SQLExpr value, SQLIntervalUnit unit){
        setValue(value);
        this.unit = unit;
    }

    public SQLIntervalExpr clone() {
        SQLIntervalExpr x = new SQLIntervalExpr();
        if (value != null) {
            x.setValue(value.clone());
        }
        x.unit = unit;
        return x;
    }

    public SQLExpr getValue() {
        return value;
    }

    public void setValue(SQLExpr value) {
        this.value = value;
    }

    public SQLIntervalUnit getUnit() {
        return unit;
    }

    public void setUnit(SQLIntervalUnit unit) {
        this.unit = unit;
    }

    @Override
    public void output(StringBuffer buf) {
        buf.append("INTERVAL ");
        value.output(buf);
        if (unit != null) {
            buf.append(' ');
            buf.append(unit.name());
        }
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.value);
        }
        visitor.endVisit(this);
    }

    @Override
    public List getChildren() {
        return Collections.singletonList(value);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((unit == null) ? 0 : unit.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SQLIntervalExpr other = (SQLIntervalExpr) obj;
        if (unit != other.unit) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

}
