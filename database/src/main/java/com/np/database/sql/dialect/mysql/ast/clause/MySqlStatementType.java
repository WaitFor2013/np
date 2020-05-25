
package com.np.database.sql.dialect.mysql.ast.clause;

import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.ast.statement.SQLBlockStatement;
import com.np.database.sql.ast.statement.SQLIfStatement;
import com.np.database.sql.ast.statement.SQLLoopStatement;
import com.np.database.sql.ast.statement.SQLSelectStatement;
import com.np.database.sql.ast.statement.SQLWhileStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;

/**
 * 
 * @author zz [455910092@qq.com]
 */
public enum MySqlStatementType {
	//select statement
	SELECT(SQLSelectStatement.class.getName()),
	//update statement
	UPDATE(MySqlUpdateStatement.class.getName()),
	//insert statement
	INSERT(MySqlInsertStatement.class.getName()),
	//delete statement
	DELETE(MySqlDeleteStatement.class.getName()),
	//while statement
	WHILE(SQLWhileStatement.class.getName()),
	//begin-end
	IF(SQLIfStatement.class.getName()),
	//begin-end
	LOOP(SQLLoopStatement.class.getName()),
	//begin-end
	BLOCK(SQLBlockStatement.class.getName()),
	//declare statement
	DECLARE(MySqlDeclareStatement.class.getName()),
	//select into
	SELECTINTO(MySqlSelectIntoStatement.class.getName()),
	//case
	CASE(MySqlCaseStatement.class.getName()),
	
	UNDEFINED,
	;
	
	
	
	public final String name;

	MySqlStatementType(){
        this(null);
    }

	MySqlStatementType(String name){
        this.name = name;
    }
	public static MySqlStatementType getType(SQLStatement stmt)
	{
		 for (MySqlStatementType type : MySqlStatementType.values()) {
             if (type.name == stmt.getClass().getName()) {
                 return type;
             }
         }
		 return UNDEFINED;
	}
}
