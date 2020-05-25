
package com.np.database.sql.dialect.oracle.ast.clause;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public class SampleClause extends OracleSQLObjectImpl {

    private boolean       block   = false;

    private List<SQLExpr> percent = new ArrayList<SQLExpr>();

    private SQLExpr       seedValue;

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public List<SQLExpr> getPercent() {
        return percent;
    }

    public void setPercent(List<SQLExpr> percent) {
        this.percent = percent;
    }

    public SQLExpr getSeedValue() {
        return seedValue;
    }

    public void setSeedValue(SQLExpr seedValue) {
        this.seedValue = seedValue;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, seedValue);
            acceptChild(visitor, percent);
        }
        visitor.endVisit(this);
    }

    public SampleClause clone() {
        SampleClause x = new SampleClause();

        x.block = block;

        for (SQLExpr item : percent) {
            SQLExpr item1 = item.clone();
            item1.setParent(x);
            x.percent.add(item1);
        }

        if (seedValue != null) {
            x.setSeedValue(seedValue.clone());
        }

        return x;
    }
}
