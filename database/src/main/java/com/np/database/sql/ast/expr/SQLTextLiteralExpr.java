
package com.np.database.sql.ast.expr;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLExprImpl;

public abstract class SQLTextLiteralExpr extends SQLExprImpl implements SQLLiteralExpr {

    protected String text;

    public SQLTextLiteralExpr(){

    }

    public SQLTextLiteralExpr(String text){

        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((text == null) ? 0 : text.hashCode());
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
        SQLTextLiteralExpr other = (SQLTextLiteralExpr) obj;
        if (text == null) {
            if (other.text != null) {
                return false;
            }
        } else if (!text.equals(other.text)) {
            return false;
        }
        return true;
    }

    public abstract SQLTextLiteralExpr clone();

    @Override
    public List getChildren() {
        return Collections.emptyList();
    }
}
