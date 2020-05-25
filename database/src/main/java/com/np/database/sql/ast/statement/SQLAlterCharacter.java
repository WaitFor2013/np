
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLAlterCharacter extends SQLObjectImpl implements SQLAlterTableItem {

    private SQLExpr characterSet;
    private SQLExpr collate;

    public void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, characterSet);
            acceptChild(visitor, collate);
        }
        visitor.endVisit(this);
    }

    public SQLExpr getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(SQLExpr characterSet) {
        this.characterSet = characterSet;
    }

    public SQLExpr getCollate() {
        return collate;
    }

    public void setCollate(SQLExpr collate) {
        this.collate = collate;
    }

}
