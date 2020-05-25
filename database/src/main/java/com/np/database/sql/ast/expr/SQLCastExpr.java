
package com.np.database.sql.ast.expr;

import java.util.Arrays;
import java.util.List;

import com.np.database.sql.ast.SQLDataType;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.ast.SQLObjectWithDataType;
import com.np.database.sql.ast.SQLReplaceable;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLCastExpr extends SQLExprImpl implements SQLObjectWithDataType, SQLReplaceable {

    protected SQLExpr     expr;
    protected SQLDataType dataType;

    public SQLCastExpr(){

    }

    public SQLExpr getExpr() {
        return this.expr;
    }

    public void setExpr(SQLExpr expr) {
        if (expr != null) {
            expr.setParent(this);
        }
        this.expr = expr;
    }

    public SQLDataType getDataType() {
        return this.dataType;
    }

    public void setDataType(SQLDataType dataType) {
        if (dataType != null) {
            dataType.setParent(this);
        }
        this.dataType = dataType;
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.expr);
            acceptChild(visitor, this.dataType);
        }
        visitor.endVisit(this);
    }

    @Override
    public boolean replace(SQLExpr expr, SQLExpr target) {
        if (this.expr == expr) {
            setExpr(target);
            return true;
        }

        return false;
    }

    @Override
    public List getChildren() {
        return Arrays.asList(this.expr, this.dataType);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
        result = prime * result + ((expr == null) ? 0 : expr.hashCode());
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
        SQLCastExpr other = (SQLCastExpr) obj;
        if (dataType == null) {
            if (other.dataType != null) {
                return false;
            }
        } else if (!dataType.equals(other.dataType)) {
            return false;
        }
        if (expr == null) {
            if (other.expr != null) {
                return false;
            }
        } else if (!expr.equals(other.expr)) {
            return false;
        }
        return true;
    }

    public SQLDataType computeDataType() {
        return dataType;
    }

    public SQLCastExpr clone() {
        SQLCastExpr x = new SQLCastExpr();
        if (expr != null) {
            x.setExpr(expr.clone());
        }
        if (dataType != null) {
            x.setDataType(dataType.clone());
        }
        return x;
    }
}
