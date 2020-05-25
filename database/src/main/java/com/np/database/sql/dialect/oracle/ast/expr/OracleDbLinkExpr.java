
package com.np.database.sql.dialect.oracle.ast.expr;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.util.FnvHash;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.util.FnvHash;
import com.np.database.sql.visitor.SQLASTVisitor;

public class OracleDbLinkExpr extends SQLExprImpl implements SQLName, OracleExpr {

    private SQLExpr expr;
    private String  dbLink;

    private long    dbLinkHashCode64;
    private long    hashCode64;


    public OracleDbLinkExpr(){

    }

    public String getSimpleName() {
        return dbLink;
    }

    public SQLExpr getExpr() {
        return this.expr;
    }

    public void setExpr(SQLExpr expr) {
        this.expr = expr;
    }

    public String getDbLink() {
        return this.dbLink;
    }

    public void setDbLink(String dbLink) {
        this.dbLink = dbLink;
    }

    public void output(StringBuffer buf) {
        this.expr.output(buf);
        buf.append("@");
        buf.append(this.dbLink);
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor) visitor);
    }

    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.expr);
        }

        visitor.endVisit(this);
    }

    @Override
    public List<SQLObject> getChildren() {
        return Collections.<SQLObject>singletonList(this.expr);
    }

    @Override
    public int hashCode() {
        long value = hashCode64();
        return (int)(value ^ (value >>> 32));
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
        OracleDbLinkExpr other = (OracleDbLinkExpr) obj;
        return this.hashCode64() == other.hashCode64();
    }

    public OracleDbLinkExpr clone() {
        OracleDbLinkExpr x = new OracleDbLinkExpr();
        if (expr != null) {
            x.setExpr(expr.clone());
        }
        x.dbLink = dbLink;
        return x;
    }

    public long nameHashCode64() {
        if (dbLinkHashCode64 == 0
                && dbLink != null) {
            dbLinkHashCode64 = FnvHash.hashCode64(dbLink);
        }
        return dbLinkHashCode64;
    }

    @Override
    public long hashCode64() {
        if (hashCode64 == 0) {
            long hash;
            if (expr instanceof SQLName) {
                hash = ((SQLName) expr).hashCode64();

                hash ^= '@';
                hash *= FnvHash.PRIME;
            } else if (expr == null){
                hash = FnvHash.BASIC;
            } else {
                hash = FnvHash.fnv1a_64_lower(expr.toString());

                hash ^= '@';
                hash *= FnvHash.PRIME;
            }
            hash = FnvHash.hashCode64(hash, dbLink);
            hashCode64 = hash;
        }

        return hashCode64;
    }
}
