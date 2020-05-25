
package com.np.database.sql.dialect.mysql.ast.statement;

import com.np.database.sql.ast.statement.SQLAlterTableItem;
import com.np.database.sql.dialect.mysql.ast.MySqlObject;
import com.np.database.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;

public class MySqlAlterTableImportTablespace extends MySqlObjectImpl implements SQLAlterTableItem, MySqlObject {

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            accept0((MySqlASTVisitor) visitor);
        } else {
            throw new IllegalArgumentException("not support visitor type : " + visitor.getClass().getName());
        }
    }

    public void accept0(MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

}
