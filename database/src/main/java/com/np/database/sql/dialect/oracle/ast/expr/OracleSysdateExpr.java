
package com.np.database.sql.dialect.oracle.ast.expr;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public class OracleSysdateExpr extends OracleSQLObjectImpl implements SQLExpr {

    private String option;

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    public OracleSysdateExpr clone() {
        OracleSysdateExpr x = new OracleSysdateExpr();
        x.option = option;
        return x;
    }

    @Override
    public List<SQLObject> getChildren() {
        return Collections.<SQLObject>emptyList();
    }

    public String toString() {
        return NpSqlHelper.toOracleString(this);
    }
}
