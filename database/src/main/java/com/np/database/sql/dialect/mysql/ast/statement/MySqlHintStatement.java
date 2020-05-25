
package com.np.database.sql.dialect.mysql.ast.statement;

import java.util.List;

import com.np.database.sql.ast.SQLCommentHint;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.SQLCommentHint;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlHintStatement extends MySqlStatementImpl {

    private List<SQLCommentHint> hints;

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.hints);
        }
        visitor.endVisit(this);
    }

    public List<SQLCommentHint> getHints() {
        return hints;
    }

    public void setHints(List<SQLCommentHint> hints) {
        this.hints = hints;
    }

}
