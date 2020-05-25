
package com.np.database.sql.dialect.mysql.ast;

import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlIgnoreIndexHint extends MySqlIndexHintImpl {

    @Override
    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, getIndexList());
        }
        visitor.endVisit(this);
    }

    public MySqlIgnoreIndexHint clone() {
        MySqlIgnoreIndexHint x = new MySqlIgnoreIndexHint();
        cloneTo(x);
        return x;
    }
}
