
package com.np.database.sql.ast.expr;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLDefaultExpr extends SQLExprImpl implements SQLLiteralExpr {

    @Override
    public boolean equals(Object o) {
        return o instanceof SQLDefaultExpr;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    public String toString() {
        return "DEFAULT";
    }

    public SQLDefaultExpr clone() {
        return new SQLDefaultExpr();
    }

    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }
}
