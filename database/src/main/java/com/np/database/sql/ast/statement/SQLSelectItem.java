
package com.np.database.sql.ast.statement;

import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLDataType;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.ast.SQLReplaceable;
import com.np.database.sql.ast.expr.SQLAllColumnExpr;
import com.np.database.sql.ast.expr.SQLIdentifierExpr;
import com.np.database.sql.ast.expr.SQLPropertyExpr;
import com.np.database.sql.dialect.oracle.ast.OracleSQLObject;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.util.FnvHash;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.util.FnvHash;
import com.np.database.sql.util.JdbcConstants;

public class SQLSelectItem extends SQLObjectImpl implements SQLReplaceable {

    protected SQLExpr expr;
    protected String  alias;

    protected boolean connectByRoot = false;

    protected transient long aliasHashCode64;

    public SQLSelectItem(){

    }

    public SQLSelectItem(SQLExpr expr){
        this(expr, null);
    }

    public SQLSelectItem(SQLExpr expr, String alias){
        this.expr = expr;
        this.alias = alias;

        if (expr != null) {
            expr.setParent(this);
        }
    }
    
    public SQLSelectItem(SQLExpr expr, String alias, boolean connectByRoot){
        this.connectByRoot = connectByRoot;
        this.expr = expr;
        this.alias = alias;
        
        if (expr != null) {
            expr.setParent(this);
        }
    }

    public SQLExpr getExpr() {
        return this.expr;
    }

    public void setExpr(SQLExpr expr) {
        if (expr != null) {
            expr.setParent(this);
        }
        this.expr = expr;
    }

    public String computeAlias() {
        String alias = this.getAlias();
        if (alias == null) {
            if (expr instanceof SQLIdentifierExpr) {
                alias = ((SQLIdentifierExpr) expr).getName();
            } else if (expr instanceof SQLPropertyExpr) {
                alias = ((SQLPropertyExpr) expr).getName();
            }
        }

        return NpSqlHelper.normalize(alias);
    }

    public SQLDataType computeDataType() {
        if (expr == null) {
            return null;
        }

        return expr.computeDataType();
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void output(StringBuffer buf) {
        if(this.connectByRoot) {
            buf.append(" CONNECT_BY_ROOT ");
        }
        this.expr.output(buf);
        if ((this.alias != null) && (this.alias.length() != 0)) {
            buf.append(" AS ");
            buf.append(this.alias);
        }
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.expr);
        }
        visitor.endVisit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((alias == null) ? 0 : alias.hashCode());
        result = prime * result + ((expr == null) ? 0 : expr.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SQLSelectItem other = (SQLSelectItem) obj;
        if (alias == null) {
            if (other.alias != null) return false;
        } else if (!alias.equals(other.alias)) return false;
        if (expr == null) {
            if (other.expr != null) return false;
        } else if (!expr.equals(other.expr)) return false;
        return true;
    }

    public boolean isConnectByRoot() {
        return connectByRoot;
    }

    public void setConnectByRoot(boolean connectByRoot) {
        this.connectByRoot = connectByRoot;
    }

    public SQLSelectItem clone() {
        SQLSelectItem x = new SQLSelectItem();
        x.alias = alias;
        if (expr != null) {
            x.setExpr(expr.clone());
        }
        x.connectByRoot = connectByRoot;
        return x;
    }

    @Override
    public boolean replace(SQLExpr expr, SQLExpr target) {
        if (this.expr == expr) {
            setExpr(target);
            return true;
        }

        return false;
    }

    public boolean match(String alias) {
        if (alias == null) {
            return false;
        }

        long hash = FnvHash.hashCode64(alias);
        return match(hash);
    }

    public long alias_hash() {
        if (this.aliasHashCode64 == 0) {
            this.aliasHashCode64 = FnvHash.hashCode64(alias);
        }
        return aliasHashCode64;
    }

    public boolean match(long alias_hash) {
        long hash = alias_hash();

        if (hash == alias_hash) {
            return true;
        }

        if (expr instanceof SQLAllColumnExpr) {
            SQLTableSource resolvedTableSource = ((SQLAllColumnExpr) expr).getResolvedTableSource();
            if (resolvedTableSource != null
                    && resolvedTableSource.findColumn(alias_hash) != null) {
                return true;
            }
            return false;
        }

        if (expr instanceof SQLIdentifierExpr) {
            return ((SQLIdentifierExpr) expr).nameHashCode64() == alias_hash;
        }

        if (expr instanceof SQLPropertyExpr) {
            String ident = ((SQLPropertyExpr) expr).getName();
            if ("*".equals(ident)) {
                SQLTableSource resolvedTableSource = ((SQLPropertyExpr) expr).getResolvedTableSource();
                if (resolvedTableSource != null
                        && resolvedTableSource.findColumn(alias_hash) != null) {
                    return true;
                }
                return false;
            }

            return ((SQLPropertyExpr) expr).nameHashCode64() == alias_hash;
        }

        return false;
    }

    public String toString() {
        String dbType = null;
        if (parent instanceof OracleSQLObject) {
            dbType = JdbcConstants.ORACLE;
        }
        return NpSqlHelper.toSQLString(this, dbType);
    }
}
