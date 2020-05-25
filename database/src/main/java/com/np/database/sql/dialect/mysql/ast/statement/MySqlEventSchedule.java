
package com.np.database.sql.dialect.mysql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlEventSchedule extends MySqlObjectImpl {
    private SQLExpr at;
    private SQLExpr every;
    private SQLExpr starts;
    private SQLExpr ends;

    @Override
    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, at);
            acceptChild(visitor, every);
            acceptChild(visitor, starts);
            acceptChild(visitor, ends);
        }
        visitor.endVisit(this);
    }

    public SQLExpr getAt() {
        return at;
    }

    public void setAt(SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.at = x;
    }

    public SQLExpr getEvery() {
        return every;
    }

    public void setEvery(SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.every = x;
    }

    public SQLExpr getStarts() {
        return starts;
    }

    public void setStarts(SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.starts = x;
    }

    public SQLExpr getEnds() {
        return ends;
    }

    public void setEnds(SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.ends = x;
    }
}
