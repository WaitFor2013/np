
package com.np.database.sql.dialect.oracle.ast;

import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLDataType;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLDataType;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;

public abstract class OracleSQLObjectImpl extends SQLObjectImpl implements OracleSQLObject {

    public OracleSQLObjectImpl(){

    }

    @Override
    protected void accept0(SQLASTVisitor v) {
        if (v instanceof OracleASTVisitor) {
            this.accept0((OracleASTVisitor) v);
        }
    }

    public abstract void accept0(OracleASTVisitor visitor);

    public OracleSQLObject clone() {
        throw new UnsupportedOperationException(this.getClass().getName());
    }

    public SQLDataType computeDataType() {
        return null;
    }

    public String toString() {
        return NpSqlHelper.toOracleString(this);
    }
}
