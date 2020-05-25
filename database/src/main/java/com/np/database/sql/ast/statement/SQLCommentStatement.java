
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLCommentStatement extends SQLStatementImpl {

    public static enum Type {
        TABLE, COLUMN
    }

    private SQLExprTableSource on;
    private Type               type;
    private SQLExpr            comment;

    public SQLExpr getComment() {
        return comment;
    }

    public void setComment(SQLExpr comment) {
        this.comment = comment;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public SQLExprTableSource getOn() {
        return on;
    }

    public void setOn(SQLExprTableSource on) {
        if (on != null) {
            on.setParent(this);
        }
        this.on = on;
    }

    public void setOn(SQLName on) {
        this.setOn(new SQLExprTableSource(on));
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, on);
            acceptChild(visitor, comment);
        }
        visitor.endVisit(this);
    }

    @Override
    public List<SQLObject> getChildren() {
        List<SQLObject> children = new ArrayList<SQLObject>();
        if (on != null) {
            children.add(on);
        }
        if (comment != null) {
            children.add(comment);
        }
        return children;
    }
}
