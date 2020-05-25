
package com.np.database.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlExecuteStatement extends MySqlStatementImpl {

    private SQLName statementName;
    private final List<SQLExpr> parameters = new ArrayList<SQLExpr>();

    public SQLName getStatementName() {
        return statementName;
    }

    public void setStatementName(SQLName statementName) {
        this.statementName = statementName;
    }

    public List<SQLExpr> getParameters() {
        return parameters;
    }

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, statementName);
            acceptChild(visitor, parameters);
        }
        visitor.endVisit(this);
    }

    @Override
    public List<SQLObject> getChildren() {
        List<SQLObject> children = new ArrayList<SQLObject>();
        if (statementName != null) {
            children.add(statementName);
        }
        children.addAll(this.parameters);
        return children;
    }
}
