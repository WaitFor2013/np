
package com.np.database.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLCommentHint;
import com.np.database.sql.ast.SQLLimit;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLOrderBy;
import com.np.database.sql.ast.statement.SQLDeleteStatement;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.ast.SQLCommentHint;
import com.np.database.sql.ast.SQLLimit;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLOrderBy;
import com.np.database.sql.ast.statement.SQLDeleteStatement;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.visitor.SQLASTVisitor;

public class MySqlDeleteStatement extends SQLDeleteStatement {

    private boolean              lowPriority        = false;
    private boolean              quick              = false;
    private boolean              ignore             = false;
    private SQLOrderBy orderBy;
    private SQLLimit limit;
    private List<SQLCommentHint> hints;
    // for petadata
    private boolean              forceAllPartitions = false;
    private SQLName forcePartition;

    public MySqlDeleteStatement(){
        super(JdbcConstants.MYSQL);
    }

    public MySqlDeleteStatement clone() {
        MySqlDeleteStatement x = new MySqlDeleteStatement();
        cloneTo(x);

        x.lowPriority = lowPriority;
        x.quick = quick;
        x.ignore = ignore;

        if (using != null) {
            x.setUsing(using.clone());
        }
        if (orderBy != null) {
            x.setOrderBy(orderBy.clone());
        }
        if (limit != null) {
            x.setLimit(limit.clone());
        }

        return x;
    }

    public List<SQLCommentHint> getHints() {
        if (hints == null) {
            hints = new ArrayList<SQLCommentHint>();
        }
        return hints;
    }
    
    public int getHintsSize() {
        if (hints == null) {
            return 0;
        }
        
        return hints.size();
    }

    public boolean isLowPriority() {
        return lowPriority;
    }

    public void setLowPriority(boolean lowPriority) {
        this.lowPriority = lowPriority;
    }

    public boolean isQuick() {
        return quick;
    }

    public void setQuick(boolean quick) {
        this.quick = quick;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public SQLOrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(SQLOrderBy orderBy) {
        this.orderBy = orderBy;
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

    public void output(StringBuffer buf) {
        new MySqlOutputVisitor(buf).visit(this);
    }

    protected void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, tableSource);
            acceptChild(visitor, where);
            acceptChild(visitor, from);
            acceptChild(visitor, using);
            acceptChild(visitor, orderBy);
            acceptChild(visitor, limit);
        }

        visitor.endVisit(this);
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
