
package com.np.database.sql.ast.expr;

import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.util.NpHexBin;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.util.NpHexBin;

import java.util.Collections;
import java.util.List;

public class SQLHexExpr extends SQLExprImpl implements SQLLiteralExpr, SQLValuableExpr {

    private final String hex;

    public SQLHexExpr(String hex){
        this.hex = hex;
    }

    public String getHex() {
        return hex;
    }

    public void output(StringBuffer buf) {
        buf.append("0x");
        buf.append(this.hex);

        String charset = (String) getAttribute("USING");
        if (charset != null) {
            buf.append(" USING ");
            buf.append(charset);
        }
    }

    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hex == null) ? 0 : hex.hashCode());
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
        SQLHexExpr other = (SQLHexExpr) obj;
        if (hex == null) {
            if (other.hex != null) {
                return false;
            }
        } else if (!hex.equals(other.hex)) {
            return false;
        }
        return true;
    }

    public byte[] toBytes() {
        return NpHexBin.decode(this.hex);
    }

    public SQLHexExpr clone () {
        return new SQLHexExpr(hex);
    }

    public byte[] getValue() {
        return toBytes();
    }

    @Override
    public List getChildren() {
        return Collections.emptyList();
    }
}
