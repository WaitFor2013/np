
package com.np.database.sql.dialect.mysql.ast.clause;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlStatementImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author zz [455910092@qq.com]
 */
public class MySqlLeaveStatement extends MySqlStatementImpl {
	
	private String labelName;

	public MySqlLeaveStatement() {

	}

	public MySqlLeaveStatement(String labelName) {
		this.labelName = labelName;
	}

	@Override
    public void accept0(MySqlASTVisitor visitor) {
		visitor.visit(this);
        visitor.endVisit(this);
    }

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	@Override
	public List<SQLObject> getChildren() {
		return Collections.<SQLObject>emptyList();
	}
    
}
