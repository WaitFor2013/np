
package com.np.database.sql.ast;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.util.FnvHash;
import com.np.database.sql.util.FnvHash;

public class SQLArrayDataType extends SQLObjectImpl implements SQLDataType {
    private String dbType;
    private SQLDataType componentType;

    public SQLArrayDataType(SQLDataType componentType) {
        setComponentType(componentType);
    }

    public SQLArrayDataType(SQLDataType componentType, String dbType) {
        this.dbType = dbType;
        setComponentType(componentType);
    }

    @Override
    public String getName() {
        return "ARRAY";
    }

    @Override
    public long nameHashCode64() {
        return FnvHash.Constants.ARRAY;
    }

    @Override
    public void setName(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SQLExpr> getArguments() {
        return Collections.emptyList();
    }

    @Override
    public Boolean getWithTimeZone() {
        return null;
    }

    @Override
    public void setWithTimeZone(Boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWithLocalTimeZone() {
        return false;
    }

    @Override
    public void setWithLocalTimeZone(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    @Override
    public String getDbType() {
        return dbType;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, componentType);
        }
        visitor.endVisit(this);
    }

    public SQLArrayDataType clone() {
        return null;
    }

    public SQLDataType getComponentType() {
        return componentType;
    }

    public void setComponentType(SQLDataType x) {
        if (x != null) {
            x.setParent(this);
        }
        this.componentType = x;
    }
}
