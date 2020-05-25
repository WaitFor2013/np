
package com.np.database.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLHint;
import com.np.database.sql.ast.statement.SQLUpdateStatement;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLHint;
import com.np.database.sql.ast.statement.SQLUpdateStatement;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.visitor.SQLASTVisitor;

public class OracleUpdateStatement extends SQLUpdateStatement implements OracleStatement {

    private final List<SQLHint> hints         = new ArrayList<SQLHint>(1);
    private boolean             only          = false;
    private String              alias;

    private List<SQLExpr>       returningInto = new ArrayList<SQLExpr>();

    public OracleUpdateStatement(){
        super (JdbcConstants.ORACLE);
    }

    public List<SQLExpr> getReturningInto() {
        return returningInto;
    }

    public void setReturningInto(List<SQLExpr> returningInto) {
        this.returningInto = returningInto;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof OracleASTVisitor) {
            accept0((OracleASTVisitor) visitor);
            return;
        }

        super.accept(visitor);
    }

    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.hints);
            acceptChild(visitor, tableSource);
            acceptChild(visitor, items);
            acceptChild(visitor, where);
            acceptChild(visitor, returning);
            acceptChild(visitor, returningInto);
        }

        visitor.endVisit(this);
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isOnly() {
        return this.only;
    }

    public void setOnly(boolean only) {
        this.only = only;
    }

    public List<SQLHint> getHints() {
        return this.hints;
    }
}
