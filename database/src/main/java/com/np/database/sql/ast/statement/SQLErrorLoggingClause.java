
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLErrorLoggingClause extends SQLObjectImpl {

    private SQLName into;
    private SQLExpr simpleExpression;
    private SQLExpr limit;

    @Override
    public void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, into);
            acceptChild(visitor, simpleExpression);
            acceptChild(visitor, limit);
        }
        visitor.endVisit(this);
    }

    public SQLName getInto() {
        return into;
    }

    public void setInto(SQLName into) {
        this.into = into;
    }

    public SQLExpr getSimpleExpression() {
        return simpleExpression;
    }

    public void setSimpleExpression(SQLExpr simpleExpression) {
        this.simpleExpression = simpleExpression;
    }

    public SQLExpr getLimit() {
        return limit;
    }

    public void setLimit(SQLExpr limit) {
        this.limit = limit;
    }

    public SQLErrorLoggingClause clone() {
        SQLErrorLoggingClause x = new SQLErrorLoggingClause();
        if (into != null) {
            x.setInto(into.clone());
        }
        if (simpleExpression != null) {
            x.setSimpleExpression(simpleExpression.clone());
        }
        if (limit != null) {
            x.setLimit(limit.clone());
        }
        return x;
    }

}
