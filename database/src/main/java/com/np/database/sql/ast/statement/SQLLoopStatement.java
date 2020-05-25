
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLLoopStatement extends SQLStatementImpl {

    private String             labelName;

    private final List<SQLStatement> statements = new ArrayList<SQLStatement>();

    @Override
    public void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, statements);
        }
        visitor.endVisit(this);
    }

    public List<SQLStatement> getStatements() {
        return statements;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public void addStatement(SQLStatement stmt) {
        if (stmt != null) {
            stmt.setParent(this);
        }
        statements.add(stmt);
    }

    @Override
    public List getChildren() {
        return statements;
    }
}
