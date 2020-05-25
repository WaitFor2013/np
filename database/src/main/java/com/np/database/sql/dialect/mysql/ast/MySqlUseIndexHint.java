
package com.np.database.sql.dialect.mysql.ast;

import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlUseIndexHint extends MySqlIndexHintImpl {

    @Override
    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, getIndexList());
        }
        visitor.endVisit(this);
    }

    public MySqlUseIndexHint clone() {
        MySqlUseIndexHint x = new MySqlUseIndexHint();
        cloneTo(x);
        return x;
    }
}
