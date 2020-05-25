
package com.np.database.sql.ast;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.statement.SQLTableElement;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.statement.SQLTableElement;

public class SQLDeclareItem extends SQLObjectImpl implements SQLObjectWithDataType {

    protected Type                  type;

    protected SQLName               name;

    protected SQLDataType           dataType;

    protected SQLExpr               value;

    protected List<SQLTableElement> tableElementList = new ArrayList<SQLTableElement>();

    protected transient SQLObject             resolvedObject;

    public SQLDeclareItem() {

    }

    public SQLDeclareItem(SQLName name, SQLDataType dataType) {
        this.setName(name);
        this.setDataType(dataType);
    }

    public SQLDeclareItem(SQLName name, SQLDataType dataType, SQLExpr value) {
        this.setName(name);
        this.setDataType(dataType);
        this.setValue(value);
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.name);
            acceptChild(visitor, this.dataType);
            acceptChild(visitor, this.value);
            acceptChild(visitor, this.tableElementList);
        }
        visitor.endVisit(this);
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

    public SQLExpr getValue() {
        return value;
    }

    public void setValue(SQLExpr value) {
        if (value != null) {
            value.setParent(this);
        }
        this.value = value;
    }

    public List<SQLTableElement> getTableElementList() {
        return tableElementList;
    }

    public void setTableElementList(List<SQLTableElement> tableElementList) {
        this.tableElementList = tableElementList;
    }

    public enum Type {
        TABLE, LOCAL, CURSOR;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public SQLObject getResolvedObject() {
        return resolvedObject;
    }

    public void setResolvedObject(SQLObject resolvedObject) {
        this.resolvedObject = resolvedObject;
    }
}
