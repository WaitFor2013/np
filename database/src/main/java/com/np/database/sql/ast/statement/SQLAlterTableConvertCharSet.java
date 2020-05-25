
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLAlterTableConvertCharSet extends SQLObjectImpl implements SQLAlterTableItem {

    private SQLExpr charset;
    private SQLExpr collate;
    
    public SQLAlterTableConvertCharSet() {
        
    }

    public SQLExpr getCharset() {
        return charset;
    }

    public void setCharset(SQLExpr charset) {
        if (charset != null) {
            charset.setParent(this);
        }
        this.charset = charset;
    }

    public SQLExpr getCollate() {
        return collate;
    }

    public void setCollate(SQLExpr collate) {
        if (collate != null) {
            collate.setParent(this);
        }
        this.collate = collate;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, charset);
            acceptChild(visitor, collate);
        }
        visitor.endVisit(this);
    }

}
