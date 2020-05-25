
package com.np.database.sql.dialect.mysql.ast.expr;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.ast.SQLOrderingSpecification;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;

public class MySqlOrderingExpr extends SQLExprImpl implements MySqlExpr {

    protected SQLExpr                  expr;
    protected SQLOrderingSpecification type;
    
    public MySqlOrderingExpr() {
        
    }
    
    public MySqlOrderingExpr(SQLExpr expr, SQLOrderingSpecification type){
        super();
        setExpr(expr);
        this.type = type;
    }

    public MySqlOrderingExpr clone() {
        MySqlOrderingExpr x = new MySqlOrderingExpr();
        if (expr != null) {
            x.setExpr(expr.clone());
        }
        x.type = type;
        return x;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        MySqlASTVisitor mysqlVisitor = (MySqlASTVisitor) visitor;
        if (mysqlVisitor.visit(this)) {
            acceptChild(visitor, this.expr);
        }

        mysqlVisitor.endVisit(this);
    }

    @Override
    public List getChildren() {
        return Collections.singletonList(this.expr);
    }

    public SQLExpr getExpr() {
        return expr;
    }

    public void setExpr(SQLExpr expr) {
        if (expr != null) {
            expr.setParent(this);
        }
        this.expr = expr;
    }

    public SQLOrderingSpecification getType() {
        return type;
    }

    public void setType(SQLOrderingSpecification type) {
        this.type = type;
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
        MySqlOrderingExpr other = (MySqlOrderingExpr) obj;
        if (expr != other.expr) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((expr == null) ? 0 : expr.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

}
