
package com.np.database.sql.ast.expr;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLUnaryExpr extends SQLExprImpl implements Serializable {

    private static final long serialVersionUID = 1L;
    private SQLExpr           expr;
    private SQLUnaryOperator  operator;

    public SQLUnaryExpr(){

    }

    public SQLUnaryExpr(SQLUnaryOperator operator, SQLExpr expr){
        this.operator = operator;
        this.setExpr(expr);
    }

    public SQLUnaryExpr clone() {
        SQLUnaryExpr x = new SQLUnaryExpr();
        if (expr != null) {
            x.setExpr(expr.clone());
        }
        x.operator = operator;
        return x;
    }

    public SQLUnaryOperator getOperator() {
        return operator;
    }

    public void setOperator(SQLUnaryOperator operator) {
        this.operator = operator;
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

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.expr);
        }

        visitor.endVisit(this);
    }

    @Override
    public List<SQLObject> getChildren() {
        return Collections.<SQLObject>singletonList(this.expr);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((expr == null) ? 0 : expr.hashCode());
        result = prime * result + ((operator == null) ? 0 : operator.hashCode());
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
        SQLUnaryExpr other = (SQLUnaryExpr) obj;
        if (expr == null) {
            if (other.expr != null) {
                return false;
            }
        } else if (!expr.equals(other.expr)) {
            return false;
        }
        if (operator != other.operator) {
            return false;
        }
        return true;
    }
}
