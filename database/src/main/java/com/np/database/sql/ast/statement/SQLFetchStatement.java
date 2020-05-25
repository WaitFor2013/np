
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLFetchStatement extends SQLStatementImpl {

    private SQLName       cursorName;

    private boolean       bulkCollect;

    private List<SQLExpr> into = new ArrayList<SQLExpr>();

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, cursorName);
            acceptChild(visitor, into);
        }
        visitor.endVisit(this);
    }

    public SQLName getCursorName() {
        return cursorName;
    }

    public void setCursorName(SQLName cursorName) {
        this.cursorName = cursorName;
    }

    public List<SQLExpr> getInto() {
        return into;
    }

    public void setInto(List<SQLExpr> into) {
        this.into = into;
    }

    public boolean isBulkCollect() {
        return bulkCollect;
    }

    public void setBulkCollect(boolean bulkCollect) {
        this.bulkCollect = bulkCollect;
    }
}
