
package com.np.database.sql.dialect.mysql.ast.statement;

import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.visitor.SQLASTVisitor;

import java.util.List;

public abstract class MySqlStatementImpl extends SQLStatementImpl implements MySqlStatement {

    public MySqlStatementImpl() {
        super(JdbcConstants.MYSQL);
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
        throw new UnsupportedOperationException(this.getClass().getName());
    }

    public List<SQLObject> getChildren() {
        throw new UnsupportedOperationException(this.getClass().getName());
    }
}
