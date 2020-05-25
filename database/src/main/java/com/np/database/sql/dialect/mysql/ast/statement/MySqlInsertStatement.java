
package com.np.database.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.statement.SQLInsertStatement;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.statement.SQLInsertStatement;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.visitor.SQLASTVisitor;

public class MySqlInsertStatement extends SQLInsertStatement {

    private boolean             lowPriority        = false;
    private boolean             delayed            = false;
    private boolean             highPriority       = false;
    private boolean             ignore             = false;
    private boolean             rollbackOnFail     = false;

    private final List<SQLExpr> duplicateKeyUpdate = new ArrayList<SQLExpr>();

    public MySqlInsertStatement() {
        dbType = JdbcConstants.MYSQL;
    }

    public void cloneTo(MySqlInsertStatement x) {
        super.cloneTo(x);
        x.lowPriority = lowPriority;
        x.delayed = delayed;
        x.highPriority = highPriority;
        x.ignore = ignore;
        x.rollbackOnFail = rollbackOnFail;

        for (SQLExpr e : duplicateKeyUpdate) {
            SQLExpr e2 = e.clone();
            e2.setParent(x);
            x.duplicateKeyUpdate.add(e2);
        }
    }

    public List<SQLExpr> getDuplicateKeyUpdate() {
        return duplicateKeyUpdate;
    }

    public boolean isLowPriority() {
        return lowPriority;
    }

    public void setLowPriority(boolean lowPriority) {
        this.lowPriority = lowPriority;
    }

    public boolean isDelayed() {
        return delayed;
    }

    public void setDelayed(boolean delayed) {
        this.delayed = delayed;
    }

    public boolean isHighPriority() {
        return highPriority;
    }

    public void setHighPriority(boolean highPriority) {
        this.highPriority = highPriority;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public boolean isRollbackOnFail() {
        return rollbackOnFail;
    }

    public void setRollbackOnFail(boolean rollbackOnFail) {
        this.rollbackOnFail = rollbackOnFail;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            accept0((MySqlASTVisitor) visitor);
        } else {
            super.accept0(visitor);
        }
    }

    public void output(StringBuffer buf) {
        new MySqlOutputVisitor(buf).visit(this);
    }

    protected void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, getTableSource());
            this.acceptChild(visitor, getColumns());
            this.acceptChild(visitor, getValuesList());
            this.acceptChild(visitor, getQuery());
            this.acceptChild(visitor, getDuplicateKeyUpdate());
        }

        visitor.endVisit(this);
    }

    public SQLInsertStatement clone() {
        MySqlInsertStatement x = new MySqlInsertStatement();
        cloneTo(x);
        return x;
    }
}
