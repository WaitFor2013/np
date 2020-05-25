
package com.np.database.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.statement.SQLExprTableSource;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.statement.SQLExprTableSource;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlOptimizeStatement extends MySqlStatementImpl {

    private boolean                          noWriteToBinlog = false;
    private boolean                          local           = false;

    protected final List<SQLExprTableSource> tableSources    = new ArrayList<SQLExprTableSource>();

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, tableSources);
        }
        visitor.endVisit(this);
    }

    public boolean isNoWriteToBinlog() {
        return noWriteToBinlog;
    }

    public void setNoWriteToBinlog(boolean noWriteToBinlog) {
        this.noWriteToBinlog = noWriteToBinlog;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public List<SQLExprTableSource> getTableSources() {
        return tableSources;
    }

    public void addTableSource(SQLExprTableSource tableSource) {
        if (tableSource != null) {
            tableSource.setParent(this);
        }
        this.tableSources.add(tableSource);
    }
}
