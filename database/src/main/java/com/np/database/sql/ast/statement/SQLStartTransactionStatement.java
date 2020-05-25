
package com.np.database.sql.ast.statement;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLCommentHint;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLStartTransactionStatement extends SQLStatementImpl {

    private boolean              consistentSnapshot = false;

    private boolean              begin              = false;
    private boolean              work               = false;
    private SQLExpr              name;

    private List<SQLCommentHint> hints;

    public boolean isConsistentSnapshot() {
        return consistentSnapshot;
    }

    public void setConsistentSnapshot(boolean consistentSnapshot) {
        this.consistentSnapshot = consistentSnapshot;
    }

    public boolean isBegin() {
        return begin;
    }

    public void setBegin(boolean begin) {
        this.begin = begin;
    }

    public boolean isWork() {
        return work;
    }

    public void setWork(boolean work) {
        this.work = work;
    }

    public void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);

        visitor.endVisit(this);
    }

    public List<SQLCommentHint> getHints() {
        return hints;
    }

    public void setHints(List<SQLCommentHint> hints) {
        this.hints = hints;
    }

    public SQLExpr getName() {
        return name;
    }

    public void setName(SQLExpr name) {
        if (name != null) {
            name.setParent(this);
        }
        this.name = name;
    }

    @Override
    public List<SQLObject> getChildren() {
        if (name != null) {
            return Collections.<SQLObject>singletonList(name);
        }
        return Collections.emptyList();
    }
}
