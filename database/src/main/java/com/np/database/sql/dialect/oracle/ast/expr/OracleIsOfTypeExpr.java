
package com.np.database.sql.dialect.oracle.ast.expr;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;

public class OracleIsOfTypeExpr extends SQLExprImpl implements OracleExpr {
    private SQLExpr expr;
    private List<SQLExpr> types = new ArrayList<SQLExpr>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OracleIsOfTypeExpr that = (OracleIsOfTypeExpr) o;

        if (expr != null ? !expr.equals(that.expr) : that.expr != null) return false;
        return types != null ? types.equals(that.types) : that.types == null;
    }

    @Override
    public int hashCode() {
        int result = expr != null ? expr.hashCode() : 0;
        result = 31 * result + (types != null ? types.hashCode() : 0);
        return result;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        accept0((OracleASTVisitor) visitor);
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, expr);
            acceptChild(visitor, types);
        }
        visitor.endVisit(this);
    }

    @Override
    public SQLExpr clone() {
        OracleIsOfTypeExpr x = new OracleIsOfTypeExpr();
        if (expr != null) {
            x.setExpr(expr);
        }
        return null;
    }

    @Override
    public List<SQLObject> getChildren() {
        List children = new ArrayList<SQLExpr>();
        if (expr != null) {
            children.add(expr);
        }
        children.addAll(types);
        return children;
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

    public List<SQLExpr> getTypes() {
        return types;
    }
}
