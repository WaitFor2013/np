
package com.np.database.sql.dialect.mysql.ast.statement;

import java.util.Arrays;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlShowCharacterSetStatement extends MySqlStatementImpl implements MySqlShowStatement {

    private SQLExpr where;
    private SQLExpr pattern;

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, where);
            acceptChild(visitor, pattern);
        }
        visitor.endVisit(this);
    }

    public SQLExpr getWhere() {
        return where;
    }

    public void setWhere(SQLExpr where) {
        this.where = where;
    }

    public SQLExpr getPattern() {
        return pattern;
    }

    public void setPattern(SQLExpr pattern) {
        this.pattern = pattern;
    }

    @Override
    public List<SQLObject> getChildren() {
        return Arrays.<SQLObject>asList(this.where, pattern);
    }

}
