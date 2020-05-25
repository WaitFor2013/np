
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLAlterProcedureStatement extends SQLStatementImpl implements SQLAlterStatement {

    private SQLExpr name;

    private boolean compile       = false;
    private boolean reuseSettings = false;

    private SQLExpr comment;
    private boolean languageSql;
    private boolean containsSql;
    private SQLExpr sqlSecurity;

    public void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, name);
        }
        visitor.endVisit(this);
    }

    public SQLExpr getName() {
        return name;
    }

    public void setName(SQLExpr name) {
        this.name = name;
    }

    public boolean isCompile() {
        return compile;
    }

    public void setCompile(boolean compile) {
        this.compile = compile;
    }

    public boolean isReuseSettings() {
        return reuseSettings;
    }

    public void setReuseSettings(boolean reuseSettings) {
        this.reuseSettings = reuseSettings;
    }

    public boolean isLanguageSql() {
        return languageSql;
    }

    public void setLanguageSql(boolean languageSql) {
        this.languageSql = languageSql;
    }

    public boolean isContainsSql() {
        return containsSql;
    }

    public void setContainsSql(boolean containsSql) {
        this.containsSql = containsSql;
    }

    public SQLExpr getSqlSecurity() {
        return sqlSecurity;
    }

    public void setSqlSecurity(SQLExpr sqlSecurity) {
        if (sqlSecurity != null) {
            sqlSecurity.setParent(this);
        }
        this.sqlSecurity = sqlSecurity;
    }

    public SQLExpr getComment() {
        return comment;
    }

    public void setComment(SQLExpr comment) {
        if (comment != null) {
            comment.setParent(this);
        }
        this.comment = comment;
    }
}
