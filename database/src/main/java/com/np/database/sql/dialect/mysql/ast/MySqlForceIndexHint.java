
package com.np.database.sql.dialect.mysql.ast;

import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlForceIndexHint extends MySqlIndexHintImpl {

    @Override
    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, getIndexList());
        }
        visitor.endVisit(this);
    }

    public MySqlForceIndexHint clone() {
        MySqlForceIndexHint x = new MySqlForceIndexHint();
        cloneTo(x);
        return x;
    }
}
