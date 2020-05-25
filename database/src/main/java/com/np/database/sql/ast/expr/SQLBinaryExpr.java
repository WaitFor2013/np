
package com.np.database.sql.ast.expr;

import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.util.NpInterUtils;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.util.NpInterUtils;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

public class SQLBinaryExpr extends SQLExprImpl implements SQLLiteralExpr, SQLValuableExpr {

    private String text;

    private transient Number val;

    public SQLBinaryExpr(){

    }

    public SQLBinaryExpr(String value){
        super();
        this.text = value;
    }

    public String getText() {
        return text;
    }

    public Number getValue() {
        if (text == null) {
            return null;
        }

        if (val == null) {
            long[] words = new long[text.length() / 64 + 1];
            for (int i = text.length() - 1; i >= 0; --i) {
                char ch = text.charAt(i);
                if (ch == '1') {
                    int wordIndex = i >> 6;
                    words[wordIndex] |= (1L << (text.length() - 1 - i));
                }
            }

            if (words.length == 1) {
                val = words[0];
            } else {
                byte[] bytes = new byte[words.length * 8];

                for (int i = 0; i < words.length; ++i) {
                    NpInterUtils.putLong(bytes, (words.length - 1 - i) * 8, words[i]);
                }

                val = new BigInteger(bytes);
            }
        }

        return val;
    }

    public void setValue(String value) {
        this.text = value;
    }

    public void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);

        visitor.endVisit(this);
    }

    public void output(StringBuffer buf) {
        buf.append("b'");
        buf.append(text);
        buf.append('\'');
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        return result;
    }

    public SQLBinaryExpr clone() {
        return new SQLBinaryExpr(text);
    }

    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
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
        SQLBinaryExpr other = (SQLBinaryExpr) obj;
        if (text == null) {
            if (other.text != null) {
                return false;
            }
        } else if (!text.equals(other.text)) {
            return false;
        }
        return true;
    }

}
