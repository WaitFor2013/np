
package com.np.database.sql.dialect.mysql.ast.clause;

import com.np.database.sql.dialect.mysql.ast.statement.MySqlStatementImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author zz [455910092@qq.com]
 */
public class MySqlIterateStatement extends MySqlStatementImpl {
	
	private String labelName;
	
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
    
}
