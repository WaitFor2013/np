
package com.np.database.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.ast.statement.SQLCreateStatement;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.ast.statement.SQLCreateStatement;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.util.JdbcConstants;

/**
 * Created by wenshao on 23/05/2017.
 */
public class OracleCreatePackageStatement extends OracleStatementImpl implements SQLCreateStatement {
    private boolean            orReplace;
    private SQLName name;

    private boolean body;

    private final List<SQLStatement> statements = new ArrayList<SQLStatement>();

    public OracleCreatePackageStatement() {
        super.setDbType(JdbcConstants.ORACLE);
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, name);
            acceptChild(visitor, statements);
        }
        visitor.endVisit(this);
    }

    public OracleCreatePackageStatement clone() {
        OracleCreatePackageStatement x = new OracleCreatePackageStatement();

        x.orReplace = orReplace;
        if (name != null) {
            x.setName(name.clone());
        }
        x.body = body;

        for (SQLStatement stmt : statements) {
            SQLStatement s2 = stmt.clone();
            s2.setParent(x);
            x.statements.add(s2);
        }

        return x;
    }

    public boolean isOrReplace() {
        return orReplace;
    }

    public void setOrReplace(boolean orReplace) {
        this.orReplace = orReplace;
    }

    public boolean isBody() {
        return body;
    }

    public void setBody(boolean body) {
        this.body = body;
    }

    public SQLName getName() {
        return name;
    }

    public void setName(SQLName name) {
        if (name != null) {
            name.setParent(this);
        }
        this.name = name;
    }

    public List<SQLStatement> getStatements() {
        return statements;
    }
}
