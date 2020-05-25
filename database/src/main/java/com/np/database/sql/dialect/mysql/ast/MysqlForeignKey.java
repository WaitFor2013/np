
package com.np.database.sql.dialect.mysql.ast;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.statement.SQLForeignKeyImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.util.JdbcConstants;

/**
 * @author kiki
 */
public class MysqlForeignKey extends SQLForeignKeyImpl {
    private SQLName  indexName;
    private boolean  hasConstraint;
    private Match    referenceMatch;
    protected Option onUpdate;
    protected Option onDelete;

    public MysqlForeignKey() {
        dbType = JdbcConstants.MYSQL;
    }

    public SQLName getIndexName() {
        return indexName;
    }

    public void setIndexName(SQLName indexName) {
        this.indexName = indexName;
    }

    public boolean isHasConstraint() {
        return hasConstraint;
    }

    public void setHasConstraint(boolean hasConstraint) {
        this.hasConstraint = hasConstraint;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            accept0((MySqlASTVisitor) visitor);
        }
    }

    protected void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.getName());
            acceptChild(visitor, this.getReferencedTableName());
            acceptChild(visitor, this.getReferencingColumns());
            acceptChild(visitor, this.getReferencedColumns());

            acceptChild(visitor, indexName);
        }
        visitor.endVisit(this);
    }

    public MysqlForeignKey clone() {
        MysqlForeignKey x = new MysqlForeignKey();
        cloneTo(x);

        x.referenceMatch = referenceMatch;
        x.onUpdate = onUpdate;
        x.onDelete = onDelete;

        return x;
    }

    public Match getReferenceMatch() {
        return referenceMatch;
    }

    public void setReferenceMatch(Match referenceMatch) {
        this.referenceMatch = referenceMatch;
    }

    public Option getOnUpdate() {
        return onUpdate;
    }

    public void setOnUpdate(Option onUpdate) {
        this.onUpdate = onUpdate;
    }

    public Option getOnDelete() {
        return onDelete;
    }

    public void setOnDelete(Option onDelete) {
        this.onDelete = onDelete;
    }

}
