
package com.np.database.sql.dialect.mysql.ast.clause;


/**
 * 
 * @author zhujun [455910092@qq.com]
 * 2016-04-16
 */
public class ConditionValue {
	// type for condition   SQLSTATE | SELF | SYSTEM | mysql_error_code
	private ConditionType type;
	
	//value for condition  condition_name | sqlstate | SQLWARNING | NOT FOUND | SQLEXCEPTION | mysql_error_code
	private String value;
	public ConditionType getType() {
		return type;
	}
	public void setType(ConditionType type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public enum ConditionType{
		SQLSTATE,
		SELF,
		SYSTEM,
		MYSQL_ERROR_CODE
	}
	
}
