
package com.np.database.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.np.database.sql.ast.SQLExpr;

public abstract class OracleSelectPivotBase extends OracleSQLObjectImpl {

    protected final List<SQLExpr> pivotFor = new ArrayList<SQLExpr>();

    public OracleSelectPivotBase(){

    }

    public List<SQLExpr> getPivotFor() {
        return this.pivotFor;
    }

    public OracleSelectPivotBase clone() {
        throw new UnsupportedOperationException();
    }
}
