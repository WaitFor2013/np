
package com.np.database.sql.dialect.mysql.ast;

import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;

public abstract class MySqlObjectImpl extends SQLObjectImpl implements MySqlObject {

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            accept0((MySqlASTVisitor) visitor);
        } else {
            throw new IllegalArgumentException("not support visitor type : " + visitor.getClass().getName());
        }
    }

    public abstract void accept0(MySqlASTVisitor visitor);
}
