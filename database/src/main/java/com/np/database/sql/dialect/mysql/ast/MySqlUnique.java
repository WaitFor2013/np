
package com.np.database.sql.dialect.mysql.ast;

import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlUnique extends MySqlKey {

    public MySqlUnique(){

    }

    protected void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.getName());
            acceptChild(visitor, this.getColumns());
        }
        visitor.endVisit(this);
    }

    public MySqlUnique clone() {
        MySqlUnique x = new MySqlUnique();
        cloneTo(x);
        return x;
    }
}
