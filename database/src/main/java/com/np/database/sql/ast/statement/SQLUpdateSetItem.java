
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.ast.SQLReplaceable;
import com.np.database.sql.ast.expr.SQLIdentifierExpr;
import com.np.database.sql.ast.expr.SQLPropertyExpr;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLUpdateSetItem extends SQLObjectImpl implements SQLReplaceable {

    private SQLExpr column;
    private SQLExpr value;

    public SQLUpdateSetItem(){

    }

    public SQLExpr getColumn() {
        return column;
    }

    public void setColumn(SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.column = x;
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

    public void output(StringBuffer buf) {
        column.output(buf);
        buf.append(" = ");
        value.output(buf);
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, column);
            acceptChild(visitor, value);
        }

        visitor.endVisit(this);
    }

    public boolean columnMatch(String column) {
        if (this.column instanceof SQLIdentifierExpr) {
            return ((SQLIdentifierExpr) this.column).nameEquals(column);
        } else if (this.column instanceof SQLPropertyExpr) {
            ((SQLPropertyExpr) this.column).nameEquals(column);
        }
        return false;
    }

    public boolean columnMatch(long columnHash) {
        if (this.column instanceof SQLName) {
            return ((SQLName) this.column).nameHashCode64() == columnHash;
        }

        return false;
    }

    @Override
    public boolean replace(SQLExpr expr, SQLExpr target) {
        if (expr == this.column) {
            this.column = target;
            return true;
        }

        if (expr == this.value) {
            this.value = target;
            return true;
        }
        return false;
    }
}
