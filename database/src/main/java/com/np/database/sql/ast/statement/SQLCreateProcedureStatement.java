
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLParameter;
import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLCreateProcedureStatement extends SQLStatementImpl implements SQLCreateStatement {

    private SQLName            definer;

    private boolean            create     = true;
    private boolean            orReplace;
    private SQLName            name;
    private SQLStatement       block;
    private List<SQLParameter> parameters = new ArrayList<SQLParameter>();

    // for oracle
    private String             javaCallSpec;

    private SQLName            authid;

    // for mysql
    private boolean            deterministic;
    private boolean            containsSql;
    private boolean            noSql;
    private boolean            readSqlData;
    private boolean            modifiesSqlData;

    private String             wrappedSource;

    @Override
    public void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, definer);
            acceptChild(visitor, name);
            acceptChild(visitor, parameters);
            acceptChild(visitor, block);
        }
        visitor.endVisit(this);
    }

    public List<SQLParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<SQLParameter> parameters) {
        this.parameters = parameters;
    }

    public SQLName getName() {
        return name;
    }

    public void setName(SQLName name) {
        this.name = name;
    }

    public SQLStatement getBlock() {
        return block;
    }

    public void setBlock(SQLStatement block) {
        if (block != null) {
            block.setParent(this);
        }
        this.block = block;
    }

    public SQLName getAuthid() {
        return authid;
    }

    public void setAuthid(SQLName authid) {
        if (authid != null) {
            authid.setParent(this);
        }
        this.authid = authid;
    }

    public boolean isOrReplace() {
        return orReplace;
    }

    public void setOrReplace(boolean orReplace) {
        this.orReplace = orReplace;
    }

    public SQLName getDefiner() {
        return definer;
    }
    
    public void setDefiner(SQLName definer) {
        this.definer = definer;
    }

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public String getJavaCallSpec() {
        return javaCallSpec;
    }

    public void setJavaCallSpec(String javaCallSpec) {
        this.javaCallSpec = javaCallSpec;
    }

    public boolean isDeterministic() {
        return deterministic;
    }

    public void setDeterministic(boolean deterministic) {
        this.deterministic = deterministic;
    }

    public boolean isContainsSql() {
        return containsSql;
    }

    public void setContainsSql(boolean containsSql) {
        this.containsSql = containsSql;
    }

    public boolean isNoSql() {
        return noSql;
    }

    public void setNoSql(boolean noSql) {
        this.noSql = noSql;
    }

    public boolean isReadSqlData() {
        return readSqlData;
    }

    public void setReadSqlData(boolean readSqlData) {
        this.readSqlData = readSqlData;
    }

    public boolean isModifiesSqlData() {
        return modifiesSqlData;
    }

    public void setModifiesSqlData(boolean modifiesSqlData) {
        this.modifiesSqlData = modifiesSqlData;
    }

    public SQLParameter findParameter(long hash) {
        for (SQLParameter param : this.parameters) {
            if (param.getName().nameHashCode64() == hash) {
                return param;
            }
        }

        return null;
    }

    public String getWrappedSource() {
        return wrappedSource;
    }

    public void setWrappedSource(String wrappedSource) {
        this.wrappedSource = wrappedSource;
    }
}
