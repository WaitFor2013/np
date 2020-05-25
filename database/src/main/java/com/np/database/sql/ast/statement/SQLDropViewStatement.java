
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLDropViewStatement extends SQLStatementImpl implements SQLDropStatement {

    protected List<SQLExprTableSource> tableSources = new ArrayList<SQLExprTableSource>();

    protected boolean                  cascade      = false;
    protected boolean                  restrict     = false;
    protected boolean                  ifExists     = false;

    public SQLDropViewStatement(){

    }
    
    public SQLDropViewStatement(String dbType){
        super (dbType);
    }

    public SQLDropViewStatement(SQLName name){
        this(new SQLExprTableSource(name));
    }

    public SQLDropViewStatement(SQLExprTableSource tableSource){
        this.tableSources.add(tableSource);
    }

    public List<SQLExprTableSource> getTableSources() {
        return tableSources;
    }
    
    public void addPartition(SQLExprTableSource tableSource) {
        if (tableSource != null) {
            tableSource.setParent(this);
        }
        this.tableSources.add(tableSource);
    }

    public void setName(SQLName name) {
        this.addTableSource(new SQLExprTableSource(name));
    }

    public void addTableSource(SQLName name) {
        this.addTableSource(new SQLExprTableSource(name));
    }

    public void addTableSource(SQLExprTableSource tableSource) {
        tableSources.add(tableSource);
    }

    public boolean isCascade() {
        return cascade;
    }

    public void setCascade(boolean cascade) {
        this.cascade = cascade;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, tableSources);
        }
        visitor.endVisit(this);
    }

    public boolean isRestrict() {
        return restrict;
    }

    public void setRestrict(boolean restrict) {
        this.restrict = restrict;
    }

    public boolean isIfExists() {
        return ifExists;
    }

    public void setIfExists(boolean ifExists) {
        this.ifExists = ifExists;
    }

    @Override
    public List getChildren() {
        return tableSources;
    }
}
