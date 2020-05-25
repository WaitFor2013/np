
package com.np.database.sql.ast.expr;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLDataType;
import com.np.database.sql.ast.SQLDataTypeImpl;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public final class SQLBooleanExpr extends SQLExprImpl implements SQLExpr, SQLLiteralExpr, SQLValuableExpr {
    public static final SQLDataType DEFAULT_DATA_TYPE = new SQLDataTypeImpl(SQLDataType.Constants.BOOLEAN);

    private boolean value;

    public SQLBooleanExpr(){

    }

    public SQLBooleanExpr(boolean value){
        this.value = value;
    }

    public boolean getBooleanValue() {
        return value;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);

        visitor.endVisit(this);
    }

    public void output(StringBuffer buf) {
        buf.append("x");
        buf.append(value ? "TRUE" : "FALSE");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (value ? 1231 : 1237);
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
        SQLBooleanExpr other = (SQLBooleanExpr) obj;
        if (value != other.value) {
            return false;
        }
        return true;
    }

    public SQLDataType computeDataType() {
        return DEFAULT_DATA_TYPE;
    }

    public SQLBooleanExpr clone() {
        return new SQLBooleanExpr(value);
    }

    @Override
    public List getChildren() {
        return Collections.emptyList();
    }

    public static enum Type {
        ON_OFF
    }
}
