
package com.np.database.sql.ast.expr;

import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.ast.statement.SQLTableSource;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.statement.SQLTableSource;

import java.util.Collections;
import java.util.List;

public final class SQLAllColumnExpr extends SQLExprImpl {
    private transient SQLTableSource resolvedTableSource;

    public SQLAllColumnExpr(){

    }

    public void output(StringBuffer buf) {
        buf.append("*");
    }

    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    public int hashCode() {
        return 0;
    }

    public boolean equals(Object o) {
        return o instanceof SQLAllColumnExpr;
    }

    public SQLAllColumnExpr clone() {
        SQLAllColumnExpr x = new SQLAllColumnExpr();

        x.resolvedTableSource = resolvedTableSource;
        return x;
    }

    public SQLTableSource getResolvedTableSource() {
        return resolvedTableSource;
    }

    public void setResolvedTableSource(SQLTableSource resolvedTableSource) {
        this.resolvedTableSource = resolvedTableSource;
    }

    @Override
    public List getChildren() {
        return Collections.emptyList();
    }
}
