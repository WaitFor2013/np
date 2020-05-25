
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLAlterTableRenameIndex extends SQLObjectImpl implements SQLAlterTableItem {

    private SQLName name;
    private SQLName to;

    public SQLAlterTableRenameIndex(SQLName name, SQLName to){
        this.setName(name);
        this.setTo(to);
    }

    public SQLAlterTableRenameIndex(){
    }


    public void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, name);
            acceptChild(visitor, to);
        }
        visitor.endVisit(this);
    }

    public SQLName getName() {
        return name;
    }

    public void setName(SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.name = x;
    }

    public SQLName getTo() {
        return to;
    }

    public void setTo(SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.to = x;
    }
}
