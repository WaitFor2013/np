
package com.np.database.sql.dialect.oracle.ast.expr;

import com.np.database.sql.ast.expr.SQLNumericLiteralExpr;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.expr.SQLNumericLiteralExpr;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;

public class OracleBinaryDoubleExpr extends SQLNumericLiteralExpr implements OracleExpr {

    private Double value;

    public OracleBinaryDoubleExpr(){

    }

    public OracleBinaryDoubleExpr(Double value){
        super();
        this.value = value;
    }

    @Override
    public Number getNumber() {
        return value;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        accept0((OracleASTVisitor) visitor);
    }

    public void accept0(OracleASTVisitor visitor) {
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
        OracleBinaryDoubleExpr other = (OracleBinaryDoubleExpr) obj;
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

        this.setValue(number.doubleValue());
    }

    public OracleBinaryDoubleExpr clone() {
        return new OracleBinaryDoubleExpr(value);
    }
}
