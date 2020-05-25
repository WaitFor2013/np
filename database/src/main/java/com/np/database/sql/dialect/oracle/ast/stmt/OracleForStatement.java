
package com.np.database.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public class OracleForStatement extends OracleStatementImpl {

    private SQLName index;

    private SQLExpr range;

    private List<SQLStatement> statements = new ArrayList<SQLStatement>();

    private boolean            all;

    private SQLName           endLabel;

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, index);
            acceptChild(visitor, range);
            acceptChild(visitor, statements);
        }
        visitor.endVisit(this);
    }

    public SQLName getIndex() {
        return index;
    }

    public void setIndex(SQLName index) {
        this.index = index;
    }

    public SQLExpr getRange() {
        return range;
    }

    public void setRange(SQLExpr range) {
        if (range != null) {
            range.setParent(this);
        }
        this.range = range;
    }

    public List<SQLStatement> getStatements() {
        return statements;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public SQLName getEndLabel() {
        return endLabel;
    }

    public void setEndLabel(SQLName endLabel) {
        if (endLabel != null) {
            endLabel.setParent(this);
        }
        this.endLabel = endLabel;
    }

    public OracleForStatement clone() {
        OracleForStatement x = new OracleForStatement();
        if (index != null) {
            x.setIndex(index.clone());
        }
        if (range != null) {
            x.setRange(range.clone());
        }
        for (SQLStatement stmt : statements) {
            SQLStatement stmt2 = stmt.clone();
            stmt2.setParent(x);
            x.statements.add(stmt2);
        }
        x.all = all;
        if (endLabel != null) {
            x.setEndLabel(endLabel.clone());
        }
        return x;
    }
}
