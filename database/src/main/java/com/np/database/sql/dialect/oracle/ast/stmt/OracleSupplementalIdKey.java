
package com.np.database.sql.dialect.oracle.ast.stmt;

import com.np.database.sql.ast.statement.SQLTableElement;
import com.np.database.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.statement.SQLTableElement;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * Created by wenshao on 20/05/2017.
 */
public class OracleSupplementalIdKey extends OracleSQLObjectImpl implements SQLTableElement {

    private boolean all;
    private boolean primaryKey;
    private boolean unique;
    private boolean uniqueIndex;
    private boolean foreignKey;

    @Override
    public void accept0(OracleASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(boolean foreignKey) {
        this.foreignKey = foreignKey;
    }

    public boolean isUniqueIndex() {
        return uniqueIndex;
    }

    public void setUniqueIndex(boolean uniqueIndex) {
        this.uniqueIndex = uniqueIndex;
    }

    public OracleSupplementalIdKey clone() {
        OracleSupplementalIdKey x = new OracleSupplementalIdKey();
        x.all = all;
        x.primaryKey = primaryKey;
        x.unique = unique;
        x.uniqueIndex = uniqueIndex;
        x.foreignKey = foreignKey;
        return x;
    }
}
