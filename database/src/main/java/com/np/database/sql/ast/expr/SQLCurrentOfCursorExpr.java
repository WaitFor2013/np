
package com.np.database.sql.ast.expr;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLCurrentOfCursorExpr extends SQLExprImpl {

    private SQLName cursorName;

    public SQLCurrentOfCursorExpr(){

    }

    public SQLCurrentOfCursorExpr(SQLName cursorName){
        this.cursorName = cursorName;
    }

    public SQLCurrentOfCursorExpr clone() {
        SQLCurrentOfCursorExpr x = new SQLCurrentOfCursorExpr();
        if (cursorName != null) {
            x.setCursorName(cursorName.clone());
        }
        return x;
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

    @Override
    public void output(StringBuffer buf) {
        buf.append("CURRENT OF ");
        cursorName.output(buf);
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.cursorName);
        }
        visitor.endVisit(this);
    }

    public List<SQLObject> getChildren() {
        return Collections.<SQLObject>singletonList(this.cursorName);
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cursorName == null) ? 0 : cursorName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SQLCurrentOfCursorExpr other = (SQLCurrentOfCursorExpr) obj;
        if (cursorName == null) {
            if (other.cursorName != null) {
                return false;
            }
        } else if (!cursorName.equals(other.cursorName)) {
            return false;
        }
        return true;
    }

}
