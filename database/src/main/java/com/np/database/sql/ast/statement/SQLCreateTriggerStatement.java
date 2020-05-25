
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLCreateTriggerStatement extends SQLStatementImpl implements SQLCreateStatement {

    private SQLName                  name;
    private boolean                  orReplace      = false;
    private TriggerType              triggerType;

    private SQLName                  definer;

    private boolean                  update;
    private boolean                  delete;
    private boolean                  insert;

    private SQLExprTableSource       on;

    private boolean                  forEachRow     = false;

    private List<SQLName>            updateOfColumns = new ArrayList<SQLName>();

    private SQLExpr                  when;
    private SQLStatement             body;
    
    public SQLCreateTriggerStatement() {
        
    }
    
    public SQLCreateTriggerStatement(String dbType) {
        super (dbType);
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, name);
            acceptChild(visitor, updateOfColumns);
            acceptChild(visitor, on);
            acceptChild(visitor, when);
            acceptChild(visitor, body);
        }
        visitor.endVisit(this);
    }

    @Override
    public List<SQLObject> getChildren() {
        List<SQLObject> children = new ArrayList<SQLObject>();
        if (name != null) {
            children.add(name);
        }
        children.addAll(updateOfColumns);
        if (on != null) {
            children.add(on);
        }
        if (when != null) {
            children.add(when);
        }
        if (body != null) {
            children.add(body);
        }
        return children;
    }

    public SQLExprTableSource getOn() {
        return on;
    }

    public void setOn(SQLName on) {
        this.setOn(new SQLExprTableSource(on));
    }

    public void setOn(SQLExprTableSource on) {
        if (on != null) {
            on.setParent(this);
        }
        this.on = on;
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

    public SQLStatement getBody() {
        return body;
    }

    public void setBody(SQLStatement body) {
        if (body != null) {
            body.setParent(this);
        }
        this.body = body;
    }

    public boolean isOrReplace() {
        return orReplace;
    }

    public void setOrReplace(boolean orReplace) {
        this.orReplace = orReplace;
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(TriggerType triggerType) {
        this.triggerType = triggerType;
    }

    public List<TriggerEvent> getTriggerEvents() {
        return null;
    }

    public boolean isForEachRow() {
        return forEachRow;
    }

    public void setForEachRow(boolean forEachRow) {
        this.forEachRow = forEachRow;
    }

    public List<SQLName> getUpdateOfColumns() {
        return updateOfColumns;
    }

    public SQLExpr getWhen() {
        return when;
    }

    public void setWhen(SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.when = x;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isInsert() {
        return insert;
    }

    public void setInsert(boolean insert) {
        this.insert = insert;
    }

    public SQLName getDefiner() {
        return definer;
    }

    public void setDefiner(SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.definer = x;
    }

    public static enum TriggerType {
        BEFORE, AFTER, INSTEAD_OF
    }

    public static enum TriggerEvent {
        INSERT, UPDATE, DELETE
    }
}
