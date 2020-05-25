
package com.np.database.sql.ast.expr;

import com.np.database.sql.ast.SQLDataType;
import com.np.database.sql.ast.SQLDataTypeImpl;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.visitor.SQLASTVisitor;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class SQLDecimalExpr
        extends SQLNumericLiteralExpr implements SQLValuableExpr, Comparable<SQLDecimalExpr>
{
    public final static SQLDataType DATA_TYPE = new SQLDataTypeImpl("DECIMAL");

    private BigDecimal value;
    private transient String literal;

    public SQLDecimalExpr(){

    }

    public SQLDecimalExpr(BigDecimal value){
        super();
        this.value = value;
    }

    public SQLDecimalExpr(String value){
        super();
        this.value = new BigDecimal(value);
        this.literal = value;
    }

    public String getLiteral() {
        return literal;
    }

    public SQLDecimalExpr clone() {
        return new SQLDecimalExpr(value);
    }

    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public Number getNumber() {
        return value;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);

        visitor.endVisit(this);
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        SQLDecimalExpr other = (SQLDecimalExpr) obj;
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public void setNumber(Number number) {
        if (number == null) {
            this.setValue(null);
            return;
        }

        this.setValue(((BigDecimal) number));
    }

    @Override
    public int compareTo(SQLDecimalExpr o) {
        return value.compareTo(o.value);
    }
}
