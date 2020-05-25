
package com.np.database.sql.ast.statement;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLCommentHint;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLSelectStatement extends SQLStatementImpl {

    protected SQLSelect select;

    public SQLSelectStatement(){

    }

    public SQLSelectStatement(String dbType){
        super (dbType);
    }

    public SQLSelectStatement(SQLSelect select){
        this.setSelect(select);
    }

    public SQLSelectStatement(SQLSelect select, String dbType){
        this(dbType);
        this.setSelect(select);
    }

    public SQLSelect getSelect() {
        return this.select;
    }

    public void setSelect(SQLSelect select) {
        if (select != null) {
            select.setParent(this);
        }
        this.select = select;
    }

    public void output(StringBuffer buf) {
        this.select.output(buf);
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.select);
        }
        visitor.endVisit(this);
    }

    public SQLSelectStatement clone() {
        SQLSelectStatement x = new SQLSelectStatement();
        if (select != null) {
            x.setSelect(select.clone());
        }
        if (headHints != null) {
            for (SQLCommentHint h : headHints) {
                SQLCommentHint h2 = h.clone();
                h2.setParent(x);
                x.headHints.add(h2);
            }
        }
        return x;
    }

    @Override
    public List<SQLObject> getChildren() {
        return Collections.<SQLObject>singletonList(select);
    }

    public boolean addWhere(SQLExpr where) {
        return select.addWhere(where);
    }
}
