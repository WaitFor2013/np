
package com.np.database.sql.dialect.mysql.ast.clause;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.expr.SQLIdentifierExpr;
import com.np.database.sql.ast.statement.SQLSelect;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlStatementImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
/**
 * 
 * @author zz [455910092@qq.com]
 */
public class MySqlCursorDeclareStatement extends MySqlStatementImpl{
	
	//cursor name
	private SQLName cursorName;
	//select statement
	private SQLSelect select;
	
	public SQLName getCursorName() {
		return cursorName;
	}
	
	public void setCursorName(SQLName cursorName) {
		if (cursorName != null) {
			cursorName.setParent(this);
		}
		this.cursorName = cursorName;
	}

	public void setCursorName(String cursorName) {
		this.setCursorName(new SQLIdentifierExpr(cursorName));
	}

	public SQLSelect getSelect() {
		return select;
	}

	public void setSelect(SQLSelect select) {
		if (select != null) {
			select.setParent(this);
		}
		this.select = select;
	}

	@Override
	public void accept0(MySqlASTVisitor visitor) {
		 if (visitor.visit(this)) {
	         acceptChild(visitor, select);
	        }
	     visitor.endVisit(this);
		
	}

}
