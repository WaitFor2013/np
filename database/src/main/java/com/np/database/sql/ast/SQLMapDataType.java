
package com.np.database.sql.ast;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.util.FnvHash;
import com.np.database.sql.util.FnvHash;

public class SQLMapDataType extends SQLObjectImpl implements SQLDataType {
    private String dbType;
    private SQLDataType keyType;
    private SQLDataType valueType;

    public SQLMapDataType() {

    }

    public SQLMapDataType(SQLDataType keyType, SQLDataType valueType) {
        this.setKeyType(keyType);
        this.setValueType(valueType);
    }

    public SQLMapDataType(SQLDataType keyType, SQLDataType valueType, String dbType) {
        this.setKeyType(keyType);
        this.setValueType(valueType);
        this.dbType = dbType;
    }

    @Override
    public String getName() {
        return "MAP";
    }

    @Override
    public long nameHashCode64() {
        return FnvHash.Constants.MAP;
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
            acceptChild(visitor, keyType);
            acceptChild(visitor, valueType);
        }
        visitor.endVisit(this);
    }

    public SQLMapDataType clone() {
        SQLMapDataType x = new SQLMapDataType();
        x.dbType = dbType;

        if (keyType != null) {
            x.setKeyType(keyType.clone());
        }

        if (valueType != null) {
            x.setValueType(valueType.clone());
        }

        return x;
    }

    public SQLDataType getKeyType() {
        return keyType;
    }

    public void setKeyType(SQLDataType x) {
        if (x != null) {
            x.setParent(this);
        }
        this.keyType = x;
    }

    public SQLDataType getValueType() {
        return valueType;
    }

    public void setValueType(SQLDataType x) {
        if (x != null) {
            x.setParent(this);
        }
        this.valueType = x;
    }
}
