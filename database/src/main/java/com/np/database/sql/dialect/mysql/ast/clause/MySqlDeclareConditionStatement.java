
package com.np.database.sql.dialect.mysql.ast.clause;


import com.np.database.sql.dialect.mysql.ast.statement.MySqlStatementImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
/**
 * 
 * @author zhujun [455910092@qq.com]
 */
public class MySqlDeclareConditionStatement extends MySqlStatementImpl{
	
	/*
	DECLARE condition_name CONDITION FOR condition_value

	condition_value:
	    SQLSTATE [VALUE] sqlstate_value
	  | mysql_error_code
	*/
	
	//condition_name
	private String conditionName; 
	//sp statement
	private ConditionValue conditionValue;
	
	public String getConditionName() {
		return conditionName;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	public ConditionValue getConditionValue() {
		return conditionValue;
	}

	public void setConditionValue(ConditionValue conditionValue) {
		this.conditionValue = conditionValue;
	}

	@Override
	public void accept0(MySqlASTVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	    visitor.endVisit(this);
		
	}

}

