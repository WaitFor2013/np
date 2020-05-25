
package com.np.database.sql.dialect.mysql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLLimit;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.statement.SQLUpdateStatement;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLLimit;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.statement.SQLUpdateStatement;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.visitor.SQLASTVisitor;

public class MySqlUpdateStatement extends SQLUpdateStatement implements MySqlStatement {
    private SQLLimit limit;

    private boolean             lowPriority        = false;
    private boolean             ignore             = false;
    private boolean             commitOnSuccess    = false;
    private boolean             rollBackOnFail     = false;
    private boolean             queryOnPk          = false;
    private SQLExpr targetAffectRow;

    // for petadata
    private boolean             forceAllPartitions = false;
    private SQLName forcePartition;

    public MySqlUpdateStatement(){
        super(JdbcConstants.MYSQL);
    }

    public SQLLimit getLimit() {
        return limit;
    }

    public void setLimit(SQLLimit limit) {
        if (limit != null) {
            limit.setParent(this);
        }
        this.limit = limit;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            accept0((MySqlASTVisitor) visitor);
        } else {
            super.accept0(visitor);
        }
    }

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, tableSource);
            acceptChild(visitor, items);
            acceptChild(visitor, where);
            acceptChild(visitor, orderBy);
            acceptChild(visitor, limit);
        }
        visitor.endVisit(this);
    }

    public boolean isLowPriority() {
        return lowPriority;
    }

    public void setLowPriority(boolean lowPriority) {
        this.lowPriority = lowPriority;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public boolean isCommitOnSuccess() {
        return commitOnSuccess;
    }

    public void setCommitOnSuccess(boolean commitOnSuccess) {
        this.commitOnSuccess = commitOnSuccess;
    }

    public boolean isRollBackOnFail() {
        return rollBackOnFail;
    }

    public void setRollBackOnFail(boolean rollBackOnFail) {
        this.rollBackOnFail = rollBackOnFail;
    }

    public boolean isQueryOnPk() {
        return queryOnPk;
    }

    public void setQueryOnPk(boolean queryOnPk) {
        this.queryOnPk = queryOnPk;
    }

    public SQLExpr getTargetAffectRow() {
        return targetAffectRow;
    }

    public void setTargetAffectRow(SQLExpr targetAffectRow) {
        if (targetAffectRow != null) {
            targetAffectRow.setParent(this);
        }
        this.targetAffectRow = targetAffectRow;
    }

    public boolean isForceAllPartitions() {
        return forceAllPartitions;
    }

    public void setForceAllPartitions(boolean forceAllPartitions) {
        this.forceAllPartitions = forceAllPartitions;
    }

    public SQLName getForcePartition() {
        return forcePartition;
    }

    public void setForcePartition(SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.forcePartition = x;
    }
}
