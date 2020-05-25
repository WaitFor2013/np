package com.np.database.sql.dialect.oracle.ast.clause;

import com.np.database.sql.ast.statement.SQLWithSubqueryClause.Entry;
import com.np.database.sql.dialect.oracle.ast.OracleSQLObject;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.statement.SQLWithSubqueryClause;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;


public class OracleWithSubqueryEntry extends SQLWithSubqueryClause.Entry implements OracleSQLObject {

    private SearchClause searchClause;
    private CycleClause  cycleClause;

    public CycleClause getCycleClause() {
        return cycleClause;
    }

    public void setCycleClause(CycleClause cycleClause) {
        if (cycleClause != null) {
            cycleClause.setParent(this);
        }
        this.cycleClause = cycleClause;
    }

    public SearchClause getSearchClause() {
        return searchClause;
    }

    public void setSearchClause(SearchClause searchClause) {
        if (searchClause != null) {
            searchClause.setParent(this);
        }
        this.searchClause = searchClause;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, columns);
            acceptChild(visitor, subQuery);
            acceptChild(visitor, searchClause);
            acceptChild(visitor, cycleClause);
        }
        visitor.endVisit(this);
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor) visitor);
    }

    public void cloneTo(OracleWithSubqueryEntry x) {
        super.cloneTo(x);

        if (searchClause != null) {
            setSearchClause(searchClause.clone());
        }

        if (cycleClause != null) {
            setCycleClause(cycleClause.clone());
        }
    }

    public OracleWithSubqueryEntry clone() {
        OracleWithSubqueryEntry x = new OracleWithSubqueryEntry();
        cloneTo(x);
        return x;
    }
}
