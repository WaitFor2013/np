
package com.np.database.sql.dialect.mysql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlShowEventsStatement extends MySqlStatementImpl implements MySqlShowStatement {

    private SQLExpr schema;
    private SQLExpr like;
    private SQLExpr where;

    public SQLExpr getSchema() {
        return schema;
    }

    public void setSchema(SQLExpr schema) {
        this.schema = schema;
    }

    public SQLExpr getLike() {
        return like;
    }

    public void setLike(SQLExpr like) {
        this.like = like;
    }

    public SQLExpr getWhere() {
        return where;
    }

    public void setWhere(SQLExpr where) {
        this.where = where;
    }

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, schema);
            acceptChild(visitor, like);
            acceptChild(visitor, where);
        }
        visitor.endVisit(this);
    }
}
