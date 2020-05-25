
package com.np.database.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.statement.SQLExprTableSource;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.statement.SQLExprTableSource;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public class OracleLockTableStatement extends OracleStatementImpl {

    private SQLExprTableSource table;
    private LockMode lockMode;
    private boolean  noWait = false;
    private SQLExpr wait;

    public boolean isNoWait() {
        return noWait;
    }

    public void setNoWait(boolean noWait) {
        this.noWait = noWait;
    }

    public SQLExpr getWait() {
        return wait;
    }

    public void setWait(SQLExpr wait) {
        this.wait = wait;
    }

    public SQLExprTableSource getTable() {
        return table;
    }

    public void setTable(SQLExprTableSource table) {
        if (table != null) {
            table.setParent(this);
        }
        this.table = table;
    }

    public void setTable(SQLName table) {
        this.setTable(new SQLExprTableSource(table));
        this.table.setParent(this);
    }

    public LockMode getLockMode() {
        return lockMode;
    }

    public void setLockMode(LockMode lockMode) {
        this.lockMode = lockMode;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, table);
            acceptChild(visitor, wait);
        }
        visitor.endVisit(this);
    }

    public List<SQLObject> getChildren() {
        List<SQLObject> children = new ArrayList<SQLObject>();
        if (table != null) {
            children.add(table);
        }
        if (wait != null) {
            children.add(wait);
        }
        return children;
    }

    public static enum LockMode {
        ROW_SHARE,
        ROW_EXCLUSIVE,
        SHARE_UPDATE,
        SHARE,
        SHARE_ROW_EXCLUSIVE,
        EXCLUSIVE,
        ;

        public String toString() {
            switch (this) {
                case ROW_SHARE:
                    return "ROW SHARE";
                case ROW_EXCLUSIVE:
                    return "ROW EXCLUSIVE";
                case SHARE_UPDATE:
                    return "SHARE UPDATE";
                case SHARE_ROW_EXCLUSIVE:
                    return "SHARE ROW EXCLUSIVE";
                default:
                    return this.name();
            }
        }
    }
}
