
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLHint;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.util.FnvHash;
import com.np.database.sql.util.FnvHash;

public abstract class SQLTableSourceImpl extends SQLObjectImpl implements SQLTableSource {
    protected String        alias;
    protected List<SQLHint> hints;
    protected SQLExpr       flashback;
    protected long          aliasHashCod64;

    public SQLTableSourceImpl(){

    }

    public SQLTableSourceImpl(String alias){
        this.alias = alias;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
        this.aliasHashCod64 = 0L;
    }

    public int getHintsSize() {
        if (hints == null) {
            return 0;
        }

        return hints.size();
    }

    public List<SQLHint> getHints() {
        if (hints == null) {
            hints = new ArrayList<SQLHint>(2);
        }
        return hints;
    }

    public void setHints(List<SQLHint> hints) {
        this.hints = hints;
    }

    public SQLTableSource clone() {
        throw new UnsupportedOperationException(this.getClass().getName());
    }

    public String computeAlias() {
        return alias;
    }

    public SQLExpr getFlashback() {
        return flashback;
    }

    public void setFlashback(SQLExpr flashback) {
        if (flashback != null) {
            flashback.setParent(this);
        }
        this.flashback = flashback;
    }

    public boolean containsAlias(String alias) {
        if (NpSqlHelper.nameEquals(this.alias, alias)) {
            return true;
        }

        return false;
    }

    public long aliasHashCode64() {
        if (aliasHashCod64 == 0
                && alias != null) {
            aliasHashCod64 = FnvHash.hashCode64(alias);
        }
        return aliasHashCod64;
    }

    public SQLColumnDefinition findColumn(String columnName) {
        if (columnName == null) {
            return null;
        }

        long hash = FnvHash.hashCode64(alias);
        return findColumn(hash);
    }

    public SQLColumnDefinition findColumn(long columnNameHash) {
        return null;
    }

    public SQLTableSource findTableSourceWithColumn(String columnName) {
        if (columnName == null) {
            return null;
        }

        long hash = FnvHash.hashCode64(alias);
        return findTableSourceWithColumn(hash);
    }

    public SQLTableSource findTableSourceWithColumn(long columnNameHash) {
        return null;
    }

    public SQLTableSource findTableSource(String alias) {
        long hash = FnvHash.hashCode64(alias);
        return findTableSource(hash);
    }

    public SQLTableSource findTableSource(long alias_hash) {
        long hash = this.aliasHashCode64();
        if (hash != 0 && hash == alias_hash) {
            return this;
        }
        return null;
    }

    public SQLObject resolveColum(long columnNameHash) {
        return findColumn(columnNameHash);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SQLTableSourceImpl that = (SQLTableSourceImpl) o;

        if (aliasHashCode64() != that.aliasHashCode64()) return false;
        if (hints != null ? !hints.equals(that.hints) : that.hints != null) return false;
        return flashback != null ? flashback.equals(that.flashback) : that.flashback == null;
    }

    @Override
    public int hashCode() {
        int result = (hints != null ? hints.hashCode() : 0);
        result = 31 * result + (flashback != null ? flashback.hashCode() : 0);
        result = 31 * result + (int) (aliasHashCode64() ^ (aliasHashCode64() >>> 32));
        return result;
    }
}
