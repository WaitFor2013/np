
package com.np.database.sql.dialect.oracle.ast.expr;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public class OracleArgumentExpr extends OracleSQLObjectImpl implements SQLExpr {

    private String  argumentName;
    private SQLExpr value;

    public OracleArgumentExpr(){

    }

    public OracleArgumentExpr(String argumentName, SQLExpr value){
        this.argumentName = argumentName;
        this.value = value;
    }

    public String getArgumentName() {
        return argumentName;
    }

    public void setArgumentName(String argumentName) {
        this.argumentName = argumentName;
    }

    public SQLExpr getValue() {
        return value;
    }

    public void setValue(SQLExpr value) {
        this.value = value;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, value);
        }
        visitor.endVisit(this);
    }

    public OracleArgumentExpr clone() {
        OracleArgumentExpr x = new OracleArgumentExpr();
        x.argumentName = argumentName;

        if (value != null) {
            x.setValue(value.clone());
        }

        return x;
    }

    @Override
    public List<SQLObject> getChildren() {
        return Collections.<SQLObject>singletonList(this.value);
    }

}
