
package com.np.database.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLLimit;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLLimit;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlShowProfileStatement extends MySqlStatementImpl implements MySqlShowStatement {

    private List<Type> types = new ArrayList<Type>();

    private SQLExpr forQuery;

    private SQLLimit limit;

    public void accept0(MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    public List<Type> getTypes() {
        return types;
    }

    public SQLExpr getForQuery() {
        return forQuery;
    }

    public void setForQuery(SQLExpr forQuery) {
        this.forQuery = forQuery;
    }

    public SQLLimit getLimit() {
        return limit;
    }

    public void setLimit(SQLLimit limit) {
        this.limit = limit;
    }

    public static enum Type {
        ALL("ALL"), BLOCK_IO("BLOCK IO"), CONTEXT_SWITCHES("CONTEXT SWITCHES"), CPU("CPU"), IPC("IPC"),
        MEMORY("MEMORY"), PAGE_FAULTS("PAGE FAULTS"), SOURCE("SOURCE"), SWAPS("SWAPS");

        public final String name;

        Type(String name){
            this.name = name;
        }
    }

}
