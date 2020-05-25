
package com.np.database.sql.ast.expr;

import com.np.database.sql.ast.SQLDataType;
import com.np.database.sql.ast.SQLDataTypeImpl;
import com.np.database.sql.util.NpInterUtils;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.util.NpInterUtils;

import java.math.BigDecimal;

public class SQLNumberExpr extends SQLNumericLiteralExpr implements SQLValuableExpr {
    public final static SQLDataType defaultDataType = new SQLDataTypeImpl("number");

    private Number number;

    private char[] chars;

    public SQLNumberExpr(){

    }

    public SQLNumberExpr(Number number){
        this.number = number;
    }

    public SQLNumberExpr(char[] chars){
        this.chars = chars;
    }

    public Number getNumber() {
        if (chars != null && number == null) {
            this.number = new BigDecimal(chars);
        }
        return this.number;
    }

    public Number getValue() {
        return getNumber();
    }

    public void setNumber(Number number) {
        this.number = number;
        this.chars = null;
    }

    public void output(StringBuilder buf) {
        if (chars != null) {
            buf.append(chars);
        } else {
            buf.append(this.number.toString());
        }
    }

    public void output(StringBuffer buf) {
        if (chars != null) {
            buf.append(chars);
        } else {
            buf.append(this.number.toString());
        }
    }

    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    @Override
    public int hashCode() {
        Number number = getNumber();
        if (number == null) {
            return 0;
        }

        return number.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (chars != null && number == null) {
            this.number = new BigDecimal(chars);
        }

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        SQLNumberExpr other = (SQLNumberExpr) obj;
        return NpInterUtils.equals(getNumber(), other.getNumber());
    }

    public SQLNumberExpr clone() {
        SQLNumberExpr x = new SQLNumberExpr();
        x.chars = chars;
        x.number = number;
        return x;
    }

    public SQLDataType computeDataType() {
        return defaultDataType;
    }
}
