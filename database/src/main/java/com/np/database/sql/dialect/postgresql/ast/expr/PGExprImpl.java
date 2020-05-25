
package com.np.database.sql.dialect.postgresql.ast.expr;

import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;


public abstract class PGExprImpl extends SQLExprImpl implements PGExpr {

    @Override
    public abstract void accept0(PGASTVisitor visitor);

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        accept0((PGASTVisitor) visitor);
    }

    public String toString() {
        return NpSqlHelper.toPGString(this);
    }
}
