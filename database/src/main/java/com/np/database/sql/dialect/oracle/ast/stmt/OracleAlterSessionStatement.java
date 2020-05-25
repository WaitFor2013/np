
package com.np.database.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.statement.SQLAssignItem;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.statement.SQLAssignItem;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public class OracleAlterSessionStatement extends OracleStatementImpl implements OracleAlterStatement {

    private List<SQLAssignItem> items = new ArrayList<SQLAssignItem>();

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, items);
        }
        visitor.endVisit(this);
    }

    public List<SQLAssignItem> getItems() {
        return items;
    }

    public void setItems(List<SQLAssignItem> items) {
        this.items = items;
    }

}
