
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLAlterTableDisableKeys extends SQLObjectImpl implements SQLAlterTableItem {

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

}
