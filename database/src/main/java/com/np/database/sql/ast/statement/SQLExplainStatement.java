
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLCommentHint;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLExplainStatement extends SQLStatementImpl {
    private String type;
    protected SQLStatement       statement;
    private List<SQLCommentHint> hints;
    
    public SQLExplainStatement() {
        
    }
    
    public SQLExplainStatement(String dbType) {
        super (dbType);
    }

    public SQLStatement getStatement() {
        return statement;
    }

    public void setStatement(SQLStatement statement) {
        if (statement != null) {
            statement.setParent(this);
        }
        this.statement = statement;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, statement);
        }
        visitor.endVisit(this);
    }

    public List<SQLCommentHint> getHints() {
        return hints;
    }

    public void setHints(List<SQLCommentHint> hints) {
        this.hints = hints;
    }

    @Override
    public List<SQLObject> getChildren() {
        List<SQLObject> children = new ArrayList<SQLObject>();
        if (statement != null) {
            children.add(statement);
        }
        return children;
    }
}
