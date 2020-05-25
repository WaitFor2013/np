
package com.np.database.sql.ast.expr;

import com.np.database.sql.ast.SQLDataType;
import com.np.database.sql.ast.SQLDataTypeImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLIntegerExpr extends SQLNumericLiteralExpr implements SQLValuableExpr{
    public static final SQLDataType DEFAULT_DATA_TYPE = new SQLDataTypeImpl("bigint");

    private Number number;

    public SQLIntegerExpr(Number number){

        this.number = number;
    }

    public SQLIntegerExpr(){

    }

    public Number getNumber() {
        return this.number;
    }

    public void setNumber(Number number) {
        this.number = number;
    }

    public void output(StringBuffer buf) {
        buf.append(this.number);
    }

    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);

        visitor.endVisit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((number == null) ? 0 : number.hashCode());
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
        SQLIntegerExpr other = (SQLIntegerExpr) obj;
        if (number == null) {
            if (other.number != null) {
                return false;
            }
        } else if (!number.equals(other.number)) {
            return false;
        }
        return true;
    }

    @Override
    public Object getValue() {
        return this.number;
    }

    public SQLIntegerExpr clone() {
        return new SQLIntegerExpr(this.number);
    }

    public SQLDataType computeDataType() {
        return DEFAULT_DATA_TYPE;
    }

}
