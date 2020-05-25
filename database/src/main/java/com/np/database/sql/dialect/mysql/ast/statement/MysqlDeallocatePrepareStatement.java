
package com.np.database.sql.dialect.mysql.ast.statement;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MysqlDeallocatePrepareStatement extends MySqlStatementImpl {
	
	private SQLName statementName;

	public SQLName getStatementName() {
		return statementName;
	}

	public void setStatementName(SQLName statementName) {
		this.statementName = statementName;
	}
	
	public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, statementName);
        }
        visitor.endVisit(this);
    }

	@Override
	public List<SQLObject> getChildren() {
		return Collections.<SQLObject>singletonList(statementName);
	}
}
