
package com.np.database.sql.ast;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.visitor.SQLASTVisitor;

public final class SQLParameter extends SQLObjectImpl implements SQLObjectWithDataType {
    private SQLName                  name;
    private SQLDataType              dataType;
    private SQLExpr                  defaultValue;
    private ParameterType            paramType;
    private boolean                  noCopy = false;
    private boolean                  constant = false;
    private SQLName                  cursorName;
    private final List<SQLParameter> cursorParameters = new ArrayList<SQLParameter>();
    private boolean                  order;
    private boolean                  map;
    private boolean                  member;

    public SQLExpr getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(SQLExpr deaultValue) {
        if (deaultValue != null) {
            deaultValue.setParent(this);
        }
        this.defaultValue = deaultValue;
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

    public SQLDataType getDataType() {
        return dataType;
    }

    public void setDataType(SQLDataType dataType) {
        if (dataType != null) {
            dataType.setParent(this);
        }
        this.dataType = dataType;
    }
    
    public ParameterType getParamType() {
        return paramType;
    }

    public void setParamType(ParameterType paramType) {
        this.paramType = paramType;
    }

    @Override
    public void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, name);
            acceptChild(visitor, dataType);
            acceptChild(visitor, defaultValue);
        }
        visitor.endVisit(this);
    }
    
    public static enum ParameterType {
        DEFAULT, //
        IN, // in
        OUT, // out
        INOUT// inout
    }

    public boolean isNoCopy() {
        return noCopy;
    }

    public void setNoCopy(boolean noCopy) {
        this.noCopy = noCopy;
    }

    public boolean isConstant() {
        return constant;
    }

    public void setConstant(boolean constant) {
        this.constant = constant;
    }

    public List<SQLParameter> getCursorParameters() {
        return cursorParameters;
    }

    public SQLName getCursorName() {
        return cursorName;
    }

    public void setCursorName(SQLName cursorName) {
        if (cursorName != null) {
            cursorName.setParent(this);
        }
        this.cursorName = cursorName;
    }

    public SQLParameter clone() {
        SQLParameter x = new SQLParameter();
        if (name != null) {
            x.setName(name.clone());
        }
        if (dataType != null) {
            x.setDataType(dataType.clone());
        }
        if (defaultValue != null) {
            x.setDefaultValue(defaultValue.clone());
        }
        x.paramType = paramType;
        x.noCopy = noCopy;
        x.constant = constant;
        x.order = order;
        x.map = map;
        if (cursorName != null) {
            x.setCursorName(cursorName.clone());
        }
        for (SQLParameter p : cursorParameters) {
            SQLParameter p2 = p.clone();
            p2.setParent(x);
            x.cursorParameters.add(p2);
        }
        return x;
    }

    public boolean isOrder() {
        return order;
    }

    public void setOrder(boolean order) {
        this.order = order;
    }

    public boolean isMap() {
        return map;
    }

    public void setMap(boolean map) {
        this.map = map;
    }

    public boolean isMember() {
        return member;
    }

    public void setMember(boolean member) {
        this.member = member;
    }
}
