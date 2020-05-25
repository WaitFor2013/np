
package com.np.database.sql.dialect.mysql.ast.statement;

import com.np.database.sql.ast.statement.SQLTableSourceImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.statement.SQLTableSourceImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;

// just for alibaba mysql
public class MySqlUpdateTableSource extends SQLTableSourceImpl {

    private MySqlUpdateStatement update;

    public MySqlUpdateTableSource(MySqlUpdateStatement update){
        this.update = update;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            accept0((MySqlASTVisitor) visitor);
        } else {
            throw new IllegalArgumentException("not support visitor type : " + visitor.getClass().getName());
        }
    }

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, update);
        }
        visitor.endVisit(this);
    }

    public MySqlUpdateStatement getUpdate() {
        return update;
    }

    public void setUpdate(MySqlUpdateStatement update) {
        this.update = update;
    }

}
