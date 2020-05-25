
package com.np.database.sql.dialect.postgresql.ast.expr;

import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.expr.SQLCastExpr;
import com.np.database.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.expr.SQLCastExpr;
import com.np.database.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;

public class PGTypeCastExpr extends SQLCastExpr implements PGExpr {

    @Override
    public void accept0(PGASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.expr);
            acceptChild(visitor, this.dataType);
        }
        visitor.endVisit(this);
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof PGASTVisitor) {
            accept0((PGASTVisitor) visitor);
            return;
        }

        super.accept0(visitor);
    }

    public String toString() {
        return NpSqlHelper.toPGString(this);
    }
}
