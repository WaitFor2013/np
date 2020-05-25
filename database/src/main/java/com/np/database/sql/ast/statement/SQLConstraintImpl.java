
package com.np.database.sql.ast.statement;

import java.util.List;

import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLCommentHint;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.ast.expr.SQLIdentifierExpr;

public abstract class SQLConstraintImpl extends SQLObjectImpl implements SQLConstraint {
    protected String  dbType;
    protected SQLName name;
    protected Boolean enable;
    protected Boolean validate;
    protected Boolean rely;
    protected SQLExpr comment;

    public List<SQLCommentHint> hints;

    public SQLConstraintImpl(){

    }

    public void cloneTo(SQLConstraintImpl x) {
        if (name != null) {
            x.setName(name.clone());
        }

        x.enable = enable;
        x.validate = validate;
        x.rely = rely;
    }

    public List<SQLCommentHint> getHints() {
        return hints;
    }

    public void setHints(List<SQLCommentHint> hints) {
        this.hints = hints;
    }


    public SQLName getName() {
        return name;
    }

    public void setName(SQLName name) {
        this.name = name;
    }

    public void setName(String name) {
        this.setName(new SQLIdentifierExpr(name));
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public void cloneTo(SQLConstraint x) {
        if (name != null) {
            x.setName(name.clone());
        }
    }

    public Boolean getValidate() {
        return validate;
    }

    public void setValidate(Boolean validate) {
        this.validate = validate;
    }

    public Boolean getRely() {
        return rely;
    }

    public void setRely(Boolean rely) {
        this.rely = rely;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public SQLExpr getComment() {
        return comment;
    }

    public void setComment(SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.comment = x;
    }

    public void simplify() {
        if (this.name instanceof SQLIdentifierExpr) {
            SQLIdentifierExpr identExpr = (SQLIdentifierExpr) this.name;
            String columnName = identExpr.getName();

            String normalized = NpSqlHelper.normalize(columnName, dbType);
            if (columnName != normalized) {
                this.setName(normalized);
            }
        }
    }
}
