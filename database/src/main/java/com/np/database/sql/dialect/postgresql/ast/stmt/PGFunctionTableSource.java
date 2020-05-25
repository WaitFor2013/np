
package com.np.database.sql.dialect.postgresql.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLParameter;
import com.np.database.sql.ast.statement.SQLExprTableSource;
import com.np.database.sql.dialect.postgresql.ast.PGSQLObject;
import com.np.database.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLParameter;
import com.np.database.sql.ast.statement.SQLExprTableSource;
import com.np.database.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;

public class PGFunctionTableSource extends SQLExprTableSource implements PGSQLObject {

    private final List<SQLParameter> parameters = new ArrayList<SQLParameter>();

    public PGFunctionTableSource(){

    }

    public PGFunctionTableSource(SQLExpr expr){
        this.expr = expr;
    }

    public List<SQLParameter> getParameters() {
        return parameters;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        this.accept0((PGASTVisitor) visitor);
    }

    public void accept0(PGASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.expr);
            acceptChild(visitor, this.parameters);
        }
        visitor.endVisit(this);
    }
}
