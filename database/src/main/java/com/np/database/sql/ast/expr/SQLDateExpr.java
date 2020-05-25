
package com.np.database.sql.ast.expr;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLDataType;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.statement.SQLCharacterDataType;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.statement.SQLCharacterDataType;

public class SQLDateExpr extends SQLExprImpl implements SQLLiteralExpr, SQLValuableExpr {
    public static final SQLDataType DEFAULT_DATA_TYPE = new SQLCharacterDataType("date");

    private SQLExpr literal;

    public SQLDateExpr(){

    }

    public SQLDateExpr(String literal) {
        this.setLiteral(literal);
    }

    public SQLExpr getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        setLiteral(new SQLCharExpr(literal));
    }

    public void setLiteral(SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.literal = x;
    }

    public String getValue() {
        if (literal instanceof SQLCharExpr) {
            return ((SQLCharExpr) literal).getText();
        }
        return null;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((literal == null) ? 0 : literal.hashCode());
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
        SQLDateExpr other = (SQLDateExpr) obj;
        if (literal == null) {
            if (other.literal != null) {
                return false;
            }
        } else if (!literal.equals(other.literal)) {
            return false;
        }
        return true;
    }

    public SQLDateExpr clone() {
        SQLDateExpr x = new SQLDateExpr();

        if (this.literal != null) {
            x.setLiteral(literal.clone());
        }

        return x;
    }

    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }
}
