
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLRollbackStatement extends SQLStatementImpl {

    private SQLName to;

    // for mysql
    private Boolean chain;
    private Boolean release;
    private SQLExpr force;
    
    public SQLRollbackStatement() {
        
    }
    
    public SQLRollbackStatement(String dbType) {
        super (dbType);
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, to);

            acceptChild(visitor, force);
        }
        visitor.endVisit(this);
    }

    @Override
    public List<SQLObject> getChildren() {
        List<SQLObject> children = new ArrayList<SQLObject>();
        if (to != null) {
            children.add(to);
        }
        if (force != null) {
            children.add(force);
        }
        return children;
    }

    public SQLName getTo() {
        return to;
    }

    public void setTo(SQLName to) {
        this.to = to;
    }

    public Boolean getChain() {
        return chain;
    }

    public void setChain(Boolean chain) {
        this.chain = chain;
    }

    public Boolean getRelease() {
        return release;
    }

    public void setRelease(Boolean release) {
        this.release = release;
    }

    public SQLExpr getForce() {
        return force;
    }

    public void setForce(SQLExpr force) {
        this.force = force;
    }

}
