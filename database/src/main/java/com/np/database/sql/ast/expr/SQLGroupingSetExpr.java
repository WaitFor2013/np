
package com.np.database.sql.ast.expr;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLGroupingSetExpr extends SQLExprImpl {

    private final List<SQLExpr> parameters = new ArrayList<SQLExpr>();

    public SQLGroupingSetExpr clone() {
        SQLGroupingSetExpr x = new SQLGroupingSetExpr();
        for (SQLExpr p : parameters) {
            SQLExpr p2 = p.clone();
            p2.setParent(x);
            x.parameters.add(p2);
        }
        return x;
    }

    public List<SQLExpr> getParameters() {
        return parameters;
    }
    
    public void addParameter(SQLExpr parameter) {
        if (parameter != null) {
            parameter.setParent(this);
        }
        this.parameters.add(parameter);
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, parameters);
        }
        visitor.endVisit(this);
    }

    @Override
    public List getChildren() {
        return this.parameters;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
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
        if (!(obj instanceof SQLGroupingSetExpr)) {
            return false;
        }
        SQLGroupingSetExpr other = (SQLGroupingSetExpr) obj;
        if (parameters == null) {
            if (other.parameters != null) {
                return false;
            }
        } else if (!parameters.equals(other.parameters)) {
            return false;
        }
        return true;
    }

}
