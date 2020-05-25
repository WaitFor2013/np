
package com.np.database.sql.dialect.mysql.ast.clause;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlStatementImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
/**
 * 
 * @author zhujun [455910092@qq.com]
 */
public class MySqlDeclareHandlerStatement extends MySqlStatementImpl{
	
	//DECLARE handler_type HANDLER FOR condition_value[,...] sp_statement
	
	//handler type
	private MySqlHandlerType handleType; 
	//sp statement
	private SQLStatement spStatement;
	
	private List<ConditionValue> conditionValues;
	
	
	public MySqlDeclareHandlerStatement() {
		conditionValues = new ArrayList<ConditionValue>();
	}

	public List<ConditionValue> getConditionValues() {
		return conditionValues;
	}

	public void setConditionValues(List<ConditionValue> conditionValues) {
		this.conditionValues = conditionValues;
	}

	public MySqlHandlerType getHandleType() {
		return handleType;
	}

	public void setHandleType(MySqlHandlerType handleType) {
		this.handleType = handleType;
	}

	public SQLStatement getSpStatement() {
		return spStatement;
	}

	public void setSpStatement(SQLStatement spStatement) {
		this.spStatement = spStatement;
	}

	@Override
	public void accept0(MySqlASTVisitor visitor) {
		if (visitor.visit(this)) {
			acceptChild(visitor, spStatement);
		}
		visitor.endVisit(this);
	}

}

