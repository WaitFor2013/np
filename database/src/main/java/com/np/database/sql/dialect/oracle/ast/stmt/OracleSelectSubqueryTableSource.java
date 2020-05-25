
package com.np.database.sql.dialect.oracle.ast.stmt;

import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.statement.SQLSelect;
import com.np.database.sql.ast.statement.SQLSubqueryTableSource;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.statement.SQLSelect;
import com.np.database.sql.ast.statement.SQLSubqueryTableSource;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;

public class OracleSelectSubqueryTableSource extends SQLSubqueryTableSource implements OracleSelectTableSource {

    protected OracleSelectPivotBase pivot;


    public OracleSelectSubqueryTableSource(){
    }

    public OracleSelectSubqueryTableSource(String alias){
        super(alias);
    }

    public OracleSelectSubqueryTableSource(SQLSelect select, String alias){
        super(select, alias);
    }

    public OracleSelectSubqueryTableSource(SQLSelect select){
        super(select);
    }

    public OracleSelectPivotBase getPivot() {
        return pivot;
    }

    public void setPivot(OracleSelectPivotBase pivot) {
        this.pivot = pivot;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor) visitor);
    }

    protected void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.getHints());
            acceptChild(visitor, this.select);
            acceptChild(visitor, this.pivot);
            acceptChild(visitor, this.flashback);
        }
        visitor.endVisit(this);
    }

    public String toString () {
        return NpSqlHelper.toOracleString(this);
    }

    public OracleSelectSubqueryTableSource clone() {
        OracleSelectSubqueryTableSource x = new OracleSelectSubqueryTableSource();
        cloneTo(x);

        if (pivot != null) {
            setParent(pivot.clone());
        }

        return x;
    }
}
