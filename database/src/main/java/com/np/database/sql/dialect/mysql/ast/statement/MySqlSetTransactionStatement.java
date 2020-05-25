
package com.np.database.sql.dialect.mysql.ast.statement;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlSetTransactionStatement extends MySqlStatementImpl {

    private Boolean global;

    private String  isolationLevel;

    private String  accessModel;

    private Boolean session;


    public Boolean getSession() {
        return session;
    }

    public void setSession(Boolean session) {
        this.session = session;
    }

    public void accept0(MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    public Boolean getGlobal() {
        return global;
    }

    public void setGlobal(Boolean global) {
        this.global = global;
    }

    public String getIsolationLevel() {
        return isolationLevel;
    }

    public void setIsolationLevel(String isolationLevel) {
        this.isolationLevel = isolationLevel;
    }

    public String getAccessModel() {
        return accessModel;
    }

    public void setAccessModel(String accessModel) {
        this.accessModel = accessModel;
    }

    @Override
    public List<SQLObject> getChildren() {
        return Collections.<SQLObject>emptyList();
    }
}
