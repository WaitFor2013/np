
package com.np.database.sql.ast;

import java.util.List;

import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.visitor.SQLASTVisitor;

public abstract class SQLStatementImpl extends SQLObjectImpl implements SQLStatement {
    protected String               dbType;
    protected boolean              afterSemi;
    protected List<SQLCommentHint> headHints;

    public SQLStatementImpl(){

    }
    
    public SQLStatementImpl(String dbType){
        this.dbType = dbType;
    }
    
    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String toString() {
        return NpSqlHelper.toSQLString(this, dbType);
    }

    public String toLowerCaseString() {
        return NpSqlHelper.toSQLString(this, dbType, NpSqlHelper.DEFAULT_LCASE_FORMAT_OPTION);
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        throw new UnsupportedOperationException(this.getClass().getName());
    }

    public List<SQLObject> getChildren() {
        throw new UnsupportedOperationException(this.getClass().getName());
    }

    public boolean isAfterSemi() {
        return afterSemi;
    }

    public void setAfterSemi(boolean afterSemi) {
        this.afterSemi = afterSemi;
    }

    public SQLStatement clone() {
        throw new UnsupportedOperationException(this.getClass().getName());
    }

    public List<SQLCommentHint> getHeadHintsDirect() {
        return headHints;
    }

    public void setHeadHints(List<SQLCommentHint> headHints) {
        this.headHints = headHints;
    }
}
