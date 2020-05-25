
package com.np.database.sql.dialect.mysql.ast;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.statement.SQLTableConstraint;
import com.np.database.sql.ast.statement.SQLUnique;
import com.np.database.sql.ast.statement.SQLUniqueConstraint;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.visitor.SQLASTVisitor;

public class MySqlKey extends SQLUnique implements SQLUniqueConstraint, SQLTableConstraint {

    private String  indexType;

    private boolean hasConstaint;

    private SQLExpr keyBlockSize;

    public MySqlKey(){
        dbType = JdbcConstants.MYSQL;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            accept0((MySqlASTVisitor) visitor);
        }
    }

    protected void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.getName());
            acceptChild(visitor, this.getColumns());
            acceptChild(visitor, name);
        }
        visitor.endVisit(this);
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    public boolean isHasConstaint() {
        return hasConstaint;
    }

    public void setHasConstaint(boolean hasConstaint) {
        this.hasConstaint = hasConstaint;
    }

    public void cloneTo(MySqlKey x) {
        super.cloneTo(x);
        x.indexType = indexType;
        x.hasConstaint = hasConstaint;
        if (keyBlockSize != null) {
            this.setKeyBlockSize(keyBlockSize.clone());
        }
    }

    public MySqlKey clone() {
        MySqlKey x = new MySqlKey();
        cloneTo(x);
        return x;
    }

    public SQLExpr getKeyBlockSize() {
        return keyBlockSize;
    }

    public void setKeyBlockSize(SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.keyBlockSize = x;
    }
}
