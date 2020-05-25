
package com.np.database.sql.ast;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.expr.SQLIdentifierExpr;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.expr.SQLIdentifierExpr;

public class SQLSubPartitionByRange extends SQLSubPartitionBy {
    private List<SQLName> columns = new ArrayList<SQLName>();

    public List<SQLName> getColumns() {
        return columns;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        
    }

    public SQLSubPartitionByRange clone() {
        SQLSubPartitionByRange x = new SQLSubPartitionByRange();

        for (SQLName column : columns) {
            SQLName c2 = column.clone();
            c2.setParent(x);
            x.columns.add(c2);
        }

        return x;
    }

    public boolean isPartitionByColumn(long columnNameHashCode64) {
        for (SQLExpr column : columns) {
            if (column instanceof SQLIdentifierExpr
                    && ((SQLIdentifierExpr) column).nameHashCode64() == columnNameHashCode64) {
                return true;
            }
        }
        return false;
    }
}
