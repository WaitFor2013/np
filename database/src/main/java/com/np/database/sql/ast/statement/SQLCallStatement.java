
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.ast.expr.SQLVariantRefExpr;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLCallStatement extends SQLStatementImpl {

    private boolean             brace      = false;

    private SQLVariantRefExpr   outParameter;

    private SQLName             procedureName;

    private final List<SQLExpr> parameters = new ArrayList<SQLExpr>();
    
    public SQLCallStatement() {
        
    }
    
    public SQLCallStatement(String dbType) {
        super (dbType);
    }

    public SQLVariantRefExpr getOutParameter() {
        return outParameter;
    }

    public void setOutParameter(SQLVariantRefExpr outParameter) {
        this.outParameter = outParameter;
    }

    public SQLName getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(SQLName procedureName) {
        this.procedureName = procedureName;
    }

    public List<SQLExpr> getParameters() {
        return parameters;
    }

    public boolean isBrace() {
        return brace;
    }

    public void setBrace(boolean brace) {
        this.brace = brace;
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.outParameter);
            acceptChild(visitor, this.procedureName);
            acceptChild(visitor, this.parameters);
        }
        visitor.endVisit(this);
    }

    @Override
    public List<SQLObject> getChildren() {
        List<SQLObject> children = new ArrayList<SQLObject>();
        children.add(outParameter);
        children.add(procedureName);
        children.addAll(parameters);
        return null;
    }
}
